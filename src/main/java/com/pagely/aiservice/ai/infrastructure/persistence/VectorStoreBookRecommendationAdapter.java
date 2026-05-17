package com.pagely.aiservice.ai.infrastructure.persistence;

import com.pagely.aiservice.ai.application.dto.result.BookRecommendationResult;
import com.pagely.aiservice.ai.application.port.out.BookRecommendationPort;
import com.pagely.aiservice.ai.domain.model.ReportAnalysis;
import com.pagely.aiservice.ai.domain.repository.ReportAnalysisRepository;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class VectorStoreBookRecommendationAdapter implements BookRecommendationPort {

    private final JdbcTemplate jdbcTemplate;
    private final ReportAnalysisRepository reportAnalysisRepository;
    private final VectorStore bookVectorStore;

    @Override
    public List<BookRecommendationResult> recommend(UUID userId) {
        List<String> readBookIds = findAlreadyReadBookIds(userId);
        String profileText = getUserProfileText(userId);

        List<Document> documents = bookVectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(profileText)
                        .topK(5 + readBookIds.size())
                        .build()
        );

        return documents.stream()
                .filter(doc -> {
                    String bookId = getMetaValue(doc.getMetadata(), "bookId");
                    return !readBookIds.contains(bookId);
                })
                .limit(5)
                .map(doc -> {
                    Map<String, Object> meta = doc.getMetadata();
                    return new BookRecommendationResult(
                            getMetaValue(meta, "bookId"),
                            getMetaValue(meta, "title"),
                            getMetaValue(meta, "author"),
                            getMetaValue(meta, "category")
                    );
                })
                .toList();
    }

    private @NonNull List<String> findAlreadyReadBookIds(UUID userId) {
        return reportAnalysisRepository.findByUserId(userId)
                .stream()
                .map(ReportAnalysis::getBookId)
                .filter(Objects::nonNull)
                .toList();
    }

    @Nullable
    private String getUserProfileText(UUID userId) {
        return jdbcTemplate.queryForObject("""
                SELECT metadata->>'profileText'
                FROM user_vector_store
                WHERE metadata->>'userId' = ?
                LIMIT 1
                """, String.class, userId.toString());
    }

    private String getMetaValue(Map<String, Object> meta, String key) {
        Object value = meta.get(key);
        if (value == null) {
            log.warn("metadata에 '{}' 키가 없습니다. 저장 시 metadata를 확인하세요.", key);
            return "";
        }
        return value.toString();
    }
}
