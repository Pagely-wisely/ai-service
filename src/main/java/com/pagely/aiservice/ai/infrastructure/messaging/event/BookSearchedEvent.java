package com.pagely.aiservice.ai.infrastructure.messaging.event;

import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BookSearchedEvent {

    private String eventId;
    private String domainType;
    private String domainId;
    private Payload payload;

    @Getter
    @NoArgsConstructor
    public static class Payload {
        private UUID userId;
        private String keyword;
        private String bookId;
        private String bookName;
        private String authors;
        private String category;
    }
}
