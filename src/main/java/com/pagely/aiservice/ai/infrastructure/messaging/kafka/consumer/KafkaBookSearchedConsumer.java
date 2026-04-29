package com.pagely.aiservice.ai.infrastructure.messaging.kafka.consumer;

import com.pagely.aiservice.ai.application.dto.command.BookSearchedCommand;
import com.pagely.aiservice.ai.application.service.UserActionService;
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

    @KafkaListener(
            topics = "book-searched",
            groupId = "ai-service"
    )
    public void consume(BookSearchedEvent event) {

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