package com.pagely.aiservice.ai.application.port.out;

public interface BookEmbeddingPort {

    void initiate(String bookId,
                  String profileText,
                  String title,
                  String author,
                  String category,
                  String description);

    void update(String bookId,
                String profileText,
                String title,
                String author,
                String category,
                String description
    );
}
