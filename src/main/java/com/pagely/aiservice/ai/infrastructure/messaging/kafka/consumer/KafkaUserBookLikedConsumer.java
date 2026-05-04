package com.pagely.aiservice.ai.infrastructure.messaging.kafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagely.aiservice.ai.application.dto.command.BookLikedCommand;
import com.pagely.aiservice.ai.application.service.UserActionService;
import com.pagely.aiservice.ai.common.InboxIdempotent;
import com.pagely.aiservice.ai.infrastructure.messaging.event.BookLikedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaUserBookLikedConsumer {

    private final UserActionService userActionService;
    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = "book-liked",
            groupId = "ai-service"
    )
    @InboxIdempotent(topic = "book-liked")
    public void consume(String strEvent) throws JsonProcessingException {

        BookLikedEvent event = objectMapper.readValue(strEvent, BookLikedEvent.class);

        log.info("BookLikedEvent 수신: {}", event.getDomainId());

        BookLikedCommand command = new BookLikedCommand(
                event.getPayload().getUserId(),
                event.getPayload().getBookId(),
                event.getPayload().getTitle(),
                event.getPayload().getAuthors(),
                event.getPayload().getCategory(),
                event.getPayload().getDescription()
        );

        userActionService.handleBookLiked(command);
    }
}