package com.pagely.aiservice.ai.infrastructure.judge;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagely.aiservice.ai.infrastructure.judge.dto.CrossValidationPipeline;
import com.pagely.aiservice.ai.infrastructure.judge.dto.JudgeRequest;
import com.pagely.aiservice.ai.infrastructure.judge.dto.JudgeVerdict;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class JudgeAspect {

    private final CrossValidationPipeline pipeline;
    private final ResourceLoader resourceLoader;
    private final ObjectMapper objectMapper;

    @Around("@annotation(judgeValidated)")
    public Object validate(ProceedingJoinPoint joinPoint,
                           JudgeValidated judgeValidated) throws Throwable {

        // 1. 기존 LLM 호출 실행
        Object result = joinPoint.proceed();

        // 2. query — 첫 번째 String 인자
        String query = extractQuery(joinPoint.getArgs());

        // 3. response — result 직렬화
        String response = serialize(result);

        // 4. systemPrompt — 어노테이션 경로로 파일 로딩
        String systemPrompt = loadPrompt(judgeValidated.systemPromptPath());

        // 5. 교차검증 실행
        JudgeRequest request = new JudgeRequest(query, response, systemPrompt, judgeValidated.rounds());
        JudgeVerdict verdict = pipeline.execute(request);

        // 6. FAIL 시 예외
        if (verdict == JudgeVerdict.FAIL) {
            throw new JudgeValidationException(query, response, verdict);
        }

        return result;
    }

    private String extractQuery(Object[] args) {
        return Arrays.stream(args)
                .filter(arg -> arg instanceof String)
                .map(Object::toString)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Judge: query 인자 없음"));
    }

    private String serialize(Object result) throws JsonProcessingException {
        return result instanceof String s ? s : objectMapper.writeValueAsString(result);
    }

    private String loadPrompt(String path) throws IOException {
        Resource resource = resourceLoader.getResource(path);
        return resource.getContentAsString(StandardCharsets.UTF_8);
    }
}