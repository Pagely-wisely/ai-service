package com.pagely.aiservice.ai.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "p_user_search_history")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserSearchHistory {

    @Id
    @UuidGenerator
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false)
    private UUID userId;

    private String bookId;

    private String title;

    private String author;

    private String category;

    private String searchKeyword;

    private LocalDateTime searchedAt;

    @Builder
    public UserSearchHistory(UUID userId,
                             String bookId,
                             String title,
                             String author,
                             String category,
                             String searchKeyword) {
        this.userId = userId;
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.category = category;
        this.searchKeyword = searchKeyword;
    }

    public static UserSearchHistory from(UUID userId,
                                         String bookId,
                                         String title,
                                         String author,
                                         String category,
                                         String searchKeyword) {
        return UserSearchHistory.builder()
                .userId(userId)
                .bookId(bookId)
                .title(title)
                .author(author)
                .category(category)
                .searchKeyword(searchKeyword)
                .build();
    }

    @PrePersist
    protected void prePersist() {
        if (searchedAt == null) {
            searchedAt = LocalDateTime.now();
        }
    }
}