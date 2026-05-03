package com.pagely.aiservice.ai.infrastructure.persistence;

import com.pagely.aiservice.ai.application.dto.result.BookRecommendationResult;
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
    public List<BookRecommendationResult> recommend(UUID userId) {
        String userVector = getUserVector(userId);

        List<String> readBookIds = reportAnalysisRepository.findByUserId(userId)
                .stream()
                .map(ReportAnalysis::getBookId)
                .filter(Objects::nonNull)
                .toList();

        return getSimilarBooks(userVector, readBookIds);
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

    private List<BookRecommendationResult> getSimilarBooks(String userVector, List<String> excludeBookIds) {
        if (excludeBookIds.isEmpty()) {
            return jdbcTemplate.query("""
                    SELECT
                        metadata->>'bookId' AS bookId,
                        metadata->>'title' AS title,
                        metadata->>'author' AS author,
                        metadata->>'category' AS category
                    FROM book_vector_store
                    WHERE metadata->>'bookId' IS NOT NULL
                    ORDER BY embedding <=> ?::vector
                    LIMIT 5
                    """,
                    (rs, rowNum) -> new BookRecommendationResult(
                            rs.getString("bookId"),
                            rs.getString("title"),
                            rs.getString("author"),
                            rs.getString("category")
                    ),
                    userVector);
        }

        return jdbcTemplate.query("""
                SELECT
                    metadata->>'bookId' AS bookId,
                    metadata->>'title' AS title,
                    metadata->>'author' AS author,
                    metadata->>'category' AS category
                FROM book_vector_store
                WHERE metadata->>'bookId' IS NOT NULL
                AND metadata->>'bookId' != ALL(?)
                ORDER BY embedding <=> ?::vector
                LIMIT 5
                """,
                (rs, rowNum) -> new BookRecommendationResult(
                        rs.getString("bookId"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("category")
                ),
                excludeBookIds.toArray(new String[0]),
                userVector);
    }
}