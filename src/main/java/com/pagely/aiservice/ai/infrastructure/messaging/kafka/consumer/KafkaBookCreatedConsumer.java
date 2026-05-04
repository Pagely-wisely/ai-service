package com.pagely.aiservice.ai.infrastructure.messaging.kafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagely.aiservice.ai.application.dto.command.BookProfileGenerateCommand;
import com.pagely.aiservice.ai.application.service.BookProfileService;
import com.pagely.aiservice.ai.common.InboxIdempotent;
import com.pagely.aiservice.ai.infrastructure.messaging.event.BookCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaBookCreatedConsumer {

    private final BookProfileService bookProfileService;
    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = "book-created",
            groupId = "ai-service"
    )
    @InboxIdempotent(topic = "book-created")
    public void consume(String strEvent) throws JsonProcessingException {
        BookCreatedEvent event = objectMapper.readValue(strEvent, BookCreatedEvent.class);
        log.info("book-created 이벤트 수신: {}", event.getDomainId());

        BookProfileGenerateCommand command = new BookProfileGenerateCommand(
                event.getPayload().getBookId(),
                event.getPayload().getTitle(),
                event.getPayload().getAuthors(),
                event.getPayload().getCategory(),
                event.getPayload().getDescription()
        );
        bookProfileService.initiateBookProfile(command);
    }
}
