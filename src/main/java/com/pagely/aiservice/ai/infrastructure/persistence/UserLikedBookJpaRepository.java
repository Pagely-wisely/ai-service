package com.pagely.aiservice.ai.infrastructure.persistence;

import com.pagely.aiservice.ai.domain.UserLikedBook;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLikedBookJpaRepository extends JpaRepository<UserLikedBook, UUID> {

    List<UserLikedBook> findByUserId(UUID userId);
}
