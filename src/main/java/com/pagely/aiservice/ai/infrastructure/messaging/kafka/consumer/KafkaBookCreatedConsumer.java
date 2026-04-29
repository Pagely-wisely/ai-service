package com.pagely.aiservice.ai.infrastructure.messaging.kafka.consumer;

import com.pagely.aiservice.ai.application.dto.command.BookProfileGenerateCommand;
import com.pagely.aiservice.ai.application.service.BookCreatedService;
import com.pagely.aiservice.ai.infrastructure.messaging.event.BookCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaBookCreatedConsumer {

    private final BookCreatedService bookCreatedService;

    @KafkaListener(
            topics = "book-created",
            groupId = "ai-service"
    )
    public void consume(BookCreatedEvent event) {

        log.info("book-created 이벤트 수신: {}", event.getDomainId());

        BookProfileGenerateCommand command = new BookProfileGenerateCommand(
                event.getPayload().getBookId(),
                event.getPayload().getTitle(),
                event.getPayload().getAuthors(),
                event.getPayload().getCategory(),
                event.getPayload().getDescription()
        );
        bookCreatedService.handleBookCreated(command);
    }
}
