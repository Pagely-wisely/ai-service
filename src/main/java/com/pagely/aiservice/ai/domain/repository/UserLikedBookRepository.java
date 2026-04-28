package com.pagely.aiservice.ai.domain.repository;

import com.pagely.aiservice.ai.domain.UserLikedBook;
import java.util.List;
import java.util.UUID;

public interface UserLikedBookRepository {

    List<UserLikedBook> findByUserId(UUID userId);
}
