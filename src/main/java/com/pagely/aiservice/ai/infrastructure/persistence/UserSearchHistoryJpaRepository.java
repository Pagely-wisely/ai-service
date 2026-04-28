package com.pagely.aiservice.ai.infrastructure.persistence;

import com.pagely.aiservice.ai.domain.UserSearchHistory;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSearchHistoryJpaRepository extends JpaRepository<UserSearchHistory, UUID> {

    List<UserSearchHistory> findByUserId(UUID userId);
}
