package com.pagely.aiservice.ai.domain.repository;

import com.pagely.aiservice.ai.domain.model.ReportAnalysis;
import java.util.List;
import java.util.UUID;

public interface ReportAnalysisRepository {

    void save(ReportAnalysis analysis);

    List<ReportAnalysis> findByBookId(String bookId);

    List<ReportAnalysis> findByUserId(UUID userId);
}
