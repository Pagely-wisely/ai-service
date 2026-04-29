package com.pagely.aiservice.ai.infrastructure.messaging.kafka.consumer;

import com.pagely.aiservice.ai.application.dto.command.BookLikedCommand;
import com.pagely.aiservice.ai.application.service.UserActionService;
import com.pagely.aiservice.ai.infrastructure.messaging.event.BookLikedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaBookLikedConsumer {

    private final UserActionService userActionService;

    @KafkaListener(
            topics = "book-liked",
            groupId = "ai-service"
    )
    public void consume(BookLikedEvent event) {

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