package com.pagely.aiservice.ai.application.service;

import com.pagely.aiservice.ai.application.dto.command.BookProfileGenerateCommand;
import com.pagely.aiservice.ai.application.port.out.BookEmbeddingPort;
import com.pagely.aiservice.ai.application.port.out.BookProfileGeneratorPort;
import com.pagely.aiservice.ai.domain.model.ReportAnalysis;
import com.pagely.aiservice.ai.domain.repository.ReportAnalysisRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookCreatedService {
    private final ReportAnalysisRepository reportAnalysisRepository;

    private final BookProfileGeneratorPort bookProfileGeneratorPort;
    private final BookEmbeddingPort bookEmbeddingPort;

    public void handleBookCreated(BookProfileGenerateCommand dto) {
        // 처음 도서 생성 커맨드가 들어올때 독후감 데이터가 있을까 ?
        List<ReportAnalysis> analysisList = reportAnalysisRepository.findByBookId(dto.bookId());

        String profileText = bookProfileGeneratorPort.generate(dto.bookId(), dto.title(), dto.author(), dto.category(), dto.description(), analysisList);

        bookEmbeddingPort.embedBookProfileText(dto.bookId(), profileText);
    }
}
