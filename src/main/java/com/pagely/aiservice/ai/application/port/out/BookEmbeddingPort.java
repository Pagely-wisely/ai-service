package com.pagely.aiservice.ai.application.port.out;

public interface BookEmbeddingPort {

    void embedBookProfileText(String bookId, String bookProfileText);

    void update(String bookId, String profileText);
}
