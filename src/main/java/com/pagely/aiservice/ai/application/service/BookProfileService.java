package com.pagely.aiservice.ai.application.service;

import com.pagely.aiservice.ai.application.port.out.BookEmbeddingPort;
import com.pagely.aiservice.ai.application.port.out.BookProfileGeneratorPort;
import com.pagely.aiservice.ai.domain.repository.ReportAnalysisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookProfileService {

    private final BookProfileGeneratorPort bookProfileGeneratorPort;
    private final BookEmbeddingPort bookEmbeddingPort;
    private final ReportAnalysisRepository reportAnalysisRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateBookProfile(String bookId,
                                  String title,
                                  String author,
                                  String category,
                                  String description) {
        String updatedProfileText = bookProfileGeneratorPort.generate(
                bookId,
                title,
                author,
                category,
                description,
                reportAnalysisRepository.findByBookId(bookId)
        );
        bookEmbeddingPort.update(bookId, updatedProfileText);
    }
}
