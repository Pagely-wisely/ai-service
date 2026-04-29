package com.pagely.aiservice.ai.infrastructure.persistence;

import com.pagely.aiservice.ai.application.port.out.BookRecommendationPort;
import com.pagely.aiservice.ai.domain.exception.AiErrorCode;
import com.pagely.aiservice.ai.domain.model.ReportAnalysis;
import com.pagely.aiservice.ai.domain.repository.ReportAnalysisRepository;
import com.pagely.common.exception.BusinessException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
@Slf4j
@Component
@RequiredArgsConstructor
public class VectorStoreBookRecommendationAdapter implements BookRecommendationPort {

    private final JdbcTemplate jdbcTemplate;
    private final ReportAnalysisRepository reportAnalysisRepository;

    @Override
    public List<String> recommend(UUID userId) {
        // 유저 벡터 가져오기
        String userVector = getUserVector(userId);
        if (userVector == null) return List.of();

        // 이미 독후감 썼으면 읽은 적 있음
        List<String> readBookIds = reportAnalysisRepository.findByUserId(userId)
                .stream()
                .map(ReportAnalysis::getBookId)
                .filter(Objects::nonNull)
                .toList();

        // 읽은책 뺴고 리턴
        return getSimilarBookIds(userVector, readBookIds);
    }

    private String getUserVector(UUID userId) {
        try {
            return jdbcTemplate.queryForObject("""
                    SELECT embedding::text
                    FROM user_vector_store
                    WHERE metadata->>'userId' = ?
                    LIMIT 1
                    """, String.class, userId.toString());
        } catch (EmptyResultDataAccessException e) {
            log.warn("유저 프로필 없음 userId: {}", userId);
            throw new BusinessException(AiErrorCode.USER_PROFILE_NOT_FOUND, e);
        }
    }

    private List<String> getSimilarBookIds(String userVector, List<String> excludeBookIds) {
        if (excludeBookIds.isEmpty()) {
            return jdbcTemplate.queryForList("""
                    SELECT metadata->>'bookId'
                    FROM book_vector_store
                    WHERE metadata->>'bookId' IS NOT NULL
                    ORDER BY embedding <=> ?::vector
                    LIMIT 5
                    """, String.class, userVector);
        }

        return jdbcTemplate.queryForList("""
                SELECT metadata->>'bookId'
                FROM book_vector_store
                WHERE metadata->>'bookId' IS NOT NULL
                AND metadata->>'bookId' != ALL(?)
                ORDER BY embedding <=> ?::vector
                LIMIT 5
                """, String.class,
                excludeBookIds.toArray(new String[0]),
                userVector);
    }
}
