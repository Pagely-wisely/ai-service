package com.pagely.aiservice.ai.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "inbox")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Inbox {

    @Id
    @Column(name = "event_id")
    private String eventId;

    @Column(nullable = false)
    private String topic;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String payload;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime processedAt;

    public static Inbox of(String eventId, String topic, String payload) {
        Inbox inbox = new Inbox();
        inbox.eventId = eventId;
        inbox.topic = topic;
        inbox.payload = payload;
        inbox.status = "RECEIVED";
        inbox.createdAt = LocalDateTime.now();
        return inbox;
    }

    public void markProcessed() {
        this.status = "PROCESSED";
        this.processedAt = LocalDateTime.now();
    }

    public void markFailed() {
        this.status = "FAILED";
        this.processedAt = LocalDateTime.now();
    }
}
