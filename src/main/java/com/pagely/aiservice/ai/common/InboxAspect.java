package com.pagely.aiservice.ai.common;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagely.aiservice.ai.domain.model.Inbox;
import com.pagely.aiservice.ai.domain.repository.InboxRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class InboxAspect {

    private final InboxRepository inboxRepository;
    private final ObjectMapper objectMapper;

    @Around("@annotation(inboxIdempotent)")
    @Transactional
    public Object process(ProceedingJoinPoint joinPoint, InboxIdempotent inboxIdempotent) throws Throwable {

        String strEvent = (String) joinPoint.getArgs()[0];

        JsonNode node = objectMapper.readTree(strEvent);
        String eventId = node.get("eventId").asText();

        if (inboxRepository.existsByEventId(eventId)) {
            log.info("{} 중복 이벤트 스킵: {}", inboxIdempotent.topic(), eventId);
            return null;
        }

        // 2. inbox 저장
        Inbox inbox = inboxRepository.save(
                Inbox.of(eventId, inboxIdempotent.topic(), strEvent)
        );

        try {
            Object result = joinPoint.proceed();

            inbox.markProcessed();
            return result;

        } catch (Exception e) {
            inbox.markFailed();
            throw e;
        }
    }
}
