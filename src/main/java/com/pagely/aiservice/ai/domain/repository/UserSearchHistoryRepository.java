package com.pagely.aiservice.ai.domain.repository;

import com.pagely.aiservice.ai.domain.UserSearchHistory;
import java.util.List;
import java.util.UUID;

public interface UserSearchHistoryRepository {

    List<UserSearchHistory> findByUserId(UUID userId);

    void save(UserSearchHistory userSearchHistory);
}
