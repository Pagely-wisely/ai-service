package com.pagely.aiservice.ai.infrastructure.messaging.kafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagely.aiservice.ai.application.dto.command.ReportCreatedCommand;
import com.pagely.aiservice.ai.application.service.ReportAnalysisService;
import com.pagely.aiservice.ai.common.InboxIdempotent;
import com.pagely.aiservice.ai.infrastructure.messaging.event.ReportCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaReportCreatedConsumer {

    private final ReportAnalysisService reportAnalysisService;
    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = "report-created",
            groupId = "ai-service"
    )
    @InboxIdempotent(topic = "report-created")
    public void consume(String strEvent) throws JsonProcessingException {
        ReportCreatedEvent event = objectMapper.readValue(strEvent, ReportCreatedEvent.class);

        log.info("독후감 생성 이벤트 수신: {}", event.getDomainId());
        ReportCreatedCommand command = new ReportCreatedCommand(
                event.getPayload().getBookId(),
                event.getPayload().getTitle(),
                event.getPayload().getAuthor(),
                event.getPayload().getCategory(),
                event.getPayload().getDescription(),
                event.getPayload().getUserId(),
                event.getPayload().getReportId(),
                event.getPayload().getReportContent()
        );

        reportAnalysisService.handleReportCreated(command);
    }
}
