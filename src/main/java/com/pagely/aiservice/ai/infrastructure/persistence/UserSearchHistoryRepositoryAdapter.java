package com.pagely.aiservice.ai.infrastructure.persistence;

import com.pagely.aiservice.ai.domain.model.UserSearchHistory;
import com.pagely.aiservice.ai.domain.repository.UserSearchHistoryRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserSearchHistoryRepositoryAdapter implements UserSearchHistoryRepository {

    private final UserSearchHistoryJpaRepository userSearchHistoryJpaRepository;

    @Override
    public List<UserSearchHistory> findByUserId(UUID userId) {
        return userSearchHistoryJpaRepository.findByUserId(userId);
    }

    @Override
    public void save(UserSearchHistory userSearchHistory) {
        userSearchHistoryJpaRepository.save(userSearchHistory);
    }
}
