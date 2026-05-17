package com.pagely.aiservice.ai.infrastructure.messaging.event;

import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BookLikedEvent {

    private String eventId;
    private String domainType;
    private String domainId;
    private Payload payload;

    @Getter
    @NoArgsConstructor
    public static class Payload {
        private UUID userId;
        private String bookId;
        private String title;
        private String category;
        private String description;
        private String authors;
    }
}
