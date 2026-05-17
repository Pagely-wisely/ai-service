package com.pagely.aiservice.ai.application.service;

import com.pagely.aiservice.ai.application.dto.command.BookLikedCommand;
import com.pagely.aiservice.ai.application.dto.command.BookSearchedCommand;
import com.pagely.aiservice.ai.application.event.UserProfileTextUpdateEvent;
import com.pagely.aiservice.ai.application.port.out.DomainEventPublisher;
import com.pagely.aiservice.ai.domain.model.UserLikedBook;
import com.pagely.aiservice.ai.domain.model.UserSearchHistory;
import com.pagely.aiservice.ai.domain.repository.UserLikedBookRepository;
import com.pagely.aiservice.ai.domain.repository.UserSearchHistoryRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserActionService {

    private final UserLikedBookRepository userLikedBookRepository;
    private final UserSearchHistoryRepository userSearchHistoryRepository;
    private final DomainEventPublisher eventPublisher;

    @Transactional
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

    @Transactional
    public void handleBookSearched(BookSearchedCommand bookSearchedCommand) {
        UserSearchHistory userSearchHistory = UserSearchHistory.from(
                bookSearchedCommand.userId(),
                bookSearchedCommand.bookId(),
                bookSearchedCommand.title(),
                bookSearchedCommand.author(),
                bookSearchedCommand.category(),
                bookSearchedCommand.searchKeyword()
        );

        userSearchHistoryRepository.save(userSearchHistory);
        publishUserProfileTextUpdateEvent(bookSearchedCommand.userId());
    }

    private void publishUserProfileTextUpdateEvent(UUID userId) {
        eventPublisher.publish(new UserProfileTextUpdateEvent(userId));
    }
}
