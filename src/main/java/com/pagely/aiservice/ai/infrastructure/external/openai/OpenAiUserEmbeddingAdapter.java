package com.pagely.aiservice.ai.infrastructure.external.openai;

import com.pagely.aiservice.ai.application.port.UserEmbeddingPort;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class OpenAiUserEmbeddingAdapter implements UserEmbeddingPort {

    private final VectorStore vectorStore;

    public OpenAiUserEmbeddingAdapter(@Qualifier("userVectorStore") VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }
    @Override
    public void update(UUID userId, String profileText) {
        // 기존 벡터 삭제
        vectorStore.delete(List.of(userId.toString()));

        // 새 프로필 저장
        Document document = new Document(
                String.valueOf(userId),
                profileText,
                Map.of("profileText", profileText)
        );
        vectorStore.add(List.of(document));
    }

    @Override
    public void initialize(UUID userId, String profileText) {
        vectorStore.delete(List.of(userId.toString()));

        Document document = new Document(
                String.valueOf(userId),
                profileText,
                Map.of(
                        "profileText", profileText
                )
        );
        vectorStore.add(List.of(document));
    }
}