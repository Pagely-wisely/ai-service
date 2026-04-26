package com.pagely.aiservice.ai.infrastructure.external.openai;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagely.aiservice.ai.application.port.out.ReportAnalysisPort;
import com.pagely.aiservice.ai.application.dto.ReportContentAnalysisResult;
import com.pagely.aiservice.ai.infrastructure.external.openai.dto.OpenAiReportContentAnalysisResponse;
import java.util.List;
import java.util.Map;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service
public class OpenAIReportAnalysisAdapter implements ReportAnalysisPort {

    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;

    public OpenAIReportAnalysisAdapter(
            @Qualifier("reportAnalysisChatClient") ChatClient chatClient,
            ObjectMapper objectMapper) {
        this.chatClient = chatClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public ReportContentAnalysisResult analyze(String reviewText) {
        String raw = callApi(reviewText);
        OpenAiReportContentAnalysisResponse response = parse(raw);
        return toApplicationDto(response);
    }

    private String callApi(String reviewText) {
        return chatClient.prompt()
                .system("""
                        당신은 도서 독후감에서 키워드를 추출하고 정제하는 전문가입니다.
                        반드시 JSON 형식으로만 응답하세요. 다른 설명은 포함하지 마세요.
                        {
                          "usedKeywords": ["텍스트에 직접 등장한 핵심 단어"],
                          "refinedKeywords": ["형용사+명사 형태로 정제된 키워드. 예: 지루한 초반, 감동적인 결말"],
                          "summarized": "해당 도서 독후감을 요약한 내용",
                          "sentiment": "POSITIVE 또는 NEGATIVE 또는 NEUTRAL"
                        }
                        """)
                .user(reviewText)
                .call()
                .content();
    }

    private OpenAiReportContentAnalysisResponse parse(String raw) {
        try {
            String json = raw
                    .replaceAll("(?s)```json\\s*", "")
                    .replaceAll("(?s)```\\s*", "")
                    .trim();

            Map<String, Object> parsed = objectMapper.readValue(
                    json, new TypeReference<>() {}
            );

            return new OpenAiReportContentAnalysisResponse(
                    (List<String>) parsed.get("usedKeywords"),
                    (List<String>) parsed.get("refinedKeywords"),
                    (String) parsed.get("summarized"),
                    (String) parsed.get("sentiment")
            );
        } catch (Exception e) {
            throw new RuntimeException("OpenAI 응답 파싱 실패: " + raw, e);
        }
    }

    private ReportContentAnalysisResult toApplicationDto(OpenAiReportContentAnalysisResponse response) {
        return new ReportContentAnalysisResult(
                response.usedKeywords(),
                response.refinedKeywords(),
                response.summarized(),
                response.sentiment()
        );
    }
}