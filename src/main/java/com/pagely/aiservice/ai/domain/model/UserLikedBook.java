package com.pagely.aiservice.ai.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "p_user_liked_book")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserLikedBook {

    @Id
    @UuidGenerator
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private String bookId;

    private String title;

    private String author;

    private String category;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Builder
    public UserLikedBook(UUID userId, String bookId, String title, String author, String category, String description) {
        this.userId = userId;
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.category = category;
        this.description = description;
    }

    public static UserLikedBook from(UUID userId,
                                     String bookId,
                                     String title,
                                     String author,
                                     String category,
                                     String description) {
        return UserLikedBook.builder()
                .userId(userId)
                .bookId(bookId)
                .title(title)
                .author(author)
                .category(category)
                .description(description)
                .build();
    }
}
