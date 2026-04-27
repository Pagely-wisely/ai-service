package com.pagely.aiservice.ai.application.service;

import com.pagely.aiservice.ai.application.dto.ReportAnalysisCompletedEvent;
import com.pagely.aiservice.ai.application.dto.ReportContentAnalysisResult;
import com.pagely.aiservice.ai.application.dto.command.ReportCreatedCommand;
import com.pagely.aiservice.ai.application.port.out.DomainEventPublisher;
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
    private final DomainEventPublisher eventPublisher;

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

        eventPublisher.publish(new ReportAnalysisCompletedEvent(
                command.bookId(),
                command.title(),
                command.author(),
                command.category(),
                command.description()
        ));
    }
}
