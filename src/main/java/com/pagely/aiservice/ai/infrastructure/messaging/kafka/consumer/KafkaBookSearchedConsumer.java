package com.pagely.aiservice.ai.infrastructure.messaging.kafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagely.aiservice.ai.application.dto.command.BookSearchedCommand;
import com.pagely.aiservice.ai.application.service.UserActionService;
import com.pagely.aiservice.ai.common.InboxIdempotent;
import com.pagely.aiservice.ai.infrastructure.messaging.event.BookSearchedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaBookSearchedConsumer {

    private final UserActionService userActionService;
    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = "book-searched",
            groupId = "ai-service"
    )
    @InboxIdempotent(topic = "book-searched")
    public void consume(String strEvent) throws JsonProcessingException {
        BookSearchedEvent event = objectMapper.readValue(strEvent, BookSearchedEvent.class);

        log.info("BookSearchedEvent 수신: {}", event.getDomainId());

        BookSearchedCommand command = new BookSearchedCommand(
                event.getPayload().getUserId(),
                event.getPayload().getBookId(),
                event.getPayload().getBookName(),
                event.getPayload().getAuthors(),
                event.getPayload().getCategory(),
                event.getPayload().getKeyword()
        );

        userActionService.handleBookSearched(command);
    }
}