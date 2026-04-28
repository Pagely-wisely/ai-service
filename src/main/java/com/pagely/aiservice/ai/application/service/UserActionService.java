package com.pagely.aiservice.ai.application.service;

import com.pagely.aiservice.ai.application.dto.command.BookLikedCommand;
import com.pagely.aiservice.ai.application.dto.command.BookSearchedCommand;
import com.pagely.aiservice.ai.application.port.out.DomainEventPublisher;
import com.pagely.aiservice.ai.domain.UserLikedBook;
import com.pagely.aiservice.ai.domain.UserSearchHistory;
import com.pagely.aiservice.ai.domain.repository.UserLikedBookRepository;
import com.pagely.aiservice.ai.domain.repository.UserSearchHistoryRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserActionService {

    private final UserLikedBookRepository userLikedBookRepository;
    private final UserSearchHistoryRepository userSearchHistoryRepository;
    private final DomainEventPublisher eventPublisher;

    public void handleBookLiked(BookLikedCommand bookLikedCommand) {
        UserLikedBook userLikedBook = UserLikedBook.builder()
                .userId(bookLikedCommand.userId())
                .bookId(bookLikedCommand.bookId())
                .title(bookLikedCommand.title())
                .author(bookLikedCommand.author())
                .category(bookLikedCommand.category())
                .description(bookLikedCommand.description())
                .build();

        userLikedBookRepository.save(userLikedBook);
        publishUserProfileTextUpdateEvent(bookLikedCommand.userId());
    }

    public void handleBookSearched(BookSearchedCommand bookSearchedCommand) {
        UserSearchHistory userSearchHistory = UserSearchHistory.builder()
                .userId(bookSearchedCommand.userId())
                .bookId(bookSearchedCommand.bookId())
                .title(bookSearchedCommand.title())
                .author(bookSearchedCommand.author())
                .category(bookSearchedCommand.category())
                .searchKeyword(bookSearchedCommand.searchKeyword())
                .build();

        userSearchHistoryRepository.save(userSearchHistory);
        publishUserProfileTextUpdateEvent(bookSearchedCommand.userId());
    }

    private void publishUserProfileTextUpdateEvent(UUID userId) {
        eventPublisher.publish(new UserProfileTextUpdateEvent(userId));
    }
}
