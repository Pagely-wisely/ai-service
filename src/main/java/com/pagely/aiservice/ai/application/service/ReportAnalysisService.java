package com.pagely.aiservice.ai.application.service;

import com.pagely.aiservice.ai.application.dto.ReportContentAnalysisResult;
import com.pagely.aiservice.ai.application.dto.command.ReportCreatedCommand;
import com.pagely.aiservice.ai.application.port.out.BookEmbeddingPort;
import com.pagely.aiservice.ai.application.port.out.BookProfileGeneratorPort;
import com.pagely.aiservice.ai.application.port.out.ReportAnalysisPort;
import com.pagely.aiservice.ai.domain.model.ReportAnalysis;
import com.pagely.aiservice.ai.domain.repository.ReportAnalysisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReportAnalysisService {

    private final ReportAnalysisRepository reportAnalysisRepository;

    private final ReportAnalysisPort reportAnalysisPort;
    private final BookProfileGeneratorPort bookProfileGeneratorPort;
    private final BookEmbeddingPort bookEmbeddingPort;

    @Transactional
    public void handleReportCreated(ReportCreatedCommand command) {
        ReportContentAnalysisResult result = reportAnalysisPort.analyze(command.reportContent());

        ReportAnalysis reportAnalysis = ReportAnalysis.builder()
                .reportId(command.reportId())
                .bookId(command.bookId())
                .keywordOriginal(result.originals())
                .keywordNormalized(result.renormalized())
                .sentiment(result.sentiment())
                .summaryText(result.summarized())
                .build();

        reportAnalysisRepository.save(reportAnalysis);

        // TODO : publish event - 도서에 대해 새롭게 생성된 독후감을 바탕으로 기존 도서의 프로필 텍스트 및 벡터값을 변경한다
        String updatedProfileText = bookProfileGeneratorPort.generate(command.bookId(), command.title(), command.author(),
                command.category(), command.description(), reportAnalysisRepository.findByBookId(command.bookId()));
        bookEmbeddingPort.update(command.bookId(), updatedProfileText);
    }
}
