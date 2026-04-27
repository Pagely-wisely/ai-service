package com.pagely.aiservice.ai.infrastructure.persistence;

import com.pagely.aiservice.ai.domain.model.ReportAnalysis;
import com.pagely.aiservice.ai.domain.repository.ReportAnalysisRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReportAnalysisRepositoryAdaptor implements ReportAnalysisRepository {

    private final ReportAnalysisJpaRepository reportAnalysisJpaRepository;

    @Override
    public void save(ReportAnalysis analysis) {
        reportAnalysisJpaRepository.save(analysis);
    }

    @Override
    public List<ReportAnalysis> findByBookId(String bookId) {
        return reportAnalysisJpaRepository.findByBookId(bookId);
    }
}
