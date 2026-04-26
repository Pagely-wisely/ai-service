package com.pagely.aiservice.ai.infrastructure.external.openai;

import com.pagely.aiservice.ai.application.port.out.BookEmbeddingPort;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class OpenAIBookEmbeddingAdapter implements BookEmbeddingPort {

    private final VectorStore vectorStore;

    public OpenAIBookEmbeddingAdapter(@Qualifier("bookVectorStore") VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @Override
    public void embedBookProfileText(String bookId, String profileText) {
        embed(bookId, profileText);
    }

    public void embed(String bookId, String profileText) {
        String uuid = UUID.nameUUIDFromBytes(("BOOK-" + bookId).getBytes()).toString();

        List<Document> documents = List.of(
                new Document(uuid, profileText, Map.of("bookId", bookId, "profileText", profileText))
        );
        vectorStore.add(documents);
    }

    @Override
    public void update(String bookId, String profileText) {
        String uuid = UUID.nameUUIDFromBytes(("BOOK-" + bookId).getBytes()).toString();

        vectorStore.delete(List.of(uuid));
        Document document = new Document(
                uuid,
                profileText,
                Map.of("bookId", bookId, "profileText", profileText)
        );
        vectorStore.add(List.of(document));
    }
}