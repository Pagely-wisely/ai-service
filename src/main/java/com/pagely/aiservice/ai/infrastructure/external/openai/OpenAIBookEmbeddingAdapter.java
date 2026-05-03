package com.pagely.aiservice.ai.infrastructure.external.openai;

import com.pagely.aiservice.ai.application.port.out.BookEmbeddingPort;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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
    public void embedBookProfileText(String bookId, String profileText,
                                     String title,
                                     String author,
                                     String category,
                                     String description) {
        String uuid = generateUUIDByBookId(bookId);
        addBookStore(bookId, profileText,title,author,category,description, uuid);
    }

    @Override
    public void update(String bookId, String profileText,
                       String title,
                       String author,
                       String category,
                       String description) {
        String uuid = generateUUIDByBookId(bookId);
        vectorStore.delete(List.of(uuid));
        addBookStore(bookId, profileText,title,author,category,description, uuid);
    }

    private static String generateUUIDByBookId(String bookId) {
        return UUID.nameUUIDFromBytes(("BOOK-" + bookId).getBytes()).toString();
    }

    private void addBookStore(String bookId, String profileText,
                              String title,
                              String author,
                              String category,
                              String description, String uuid) {
        Document document = new Document(
                uuid,
                profileText,
                Map.of("bookId", bookId,
                        "profileText", profileText,
                        "title", title,
                        "author", author,
                        "category", category,
                        "description", description
                )
        );
        vectorStore.add(List.of(document));
    }
}
