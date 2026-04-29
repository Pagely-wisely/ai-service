package com.pagely.aiservice.ai.infrastructure.messaging.event;

import lombok.Getter;

@Getter
public class BookCreatedEvent {

    private String eventId;
    private String domainType;
    private String domainId;
    private Payload payload;

    @Getter
    public static class Payload {
        private String bookId;
        private String title;
        private String category;
        private String description;
        private String authors;
    }
}
