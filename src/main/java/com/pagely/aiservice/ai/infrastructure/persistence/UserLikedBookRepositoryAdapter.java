package com.pagely.aiservice.ai.infrastructure.persistence;

import com.pagely.aiservice.ai.domain.UserLikedBook;
import com.pagely.aiservice.ai.domain.repository.UserLikedBookRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserLikedBookRepositoryAdapter implements UserLikedBookRepository {

    private final UserLikedBookRepository userLikedBookRepository;

    @Override
    public List<UserLikedBook> findByUserId(UUID userId) {
        return userLikedBookRepository.findByUserId(userId);
    }
}
