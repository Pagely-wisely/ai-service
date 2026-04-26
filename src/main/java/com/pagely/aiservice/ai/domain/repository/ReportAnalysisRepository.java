package com.pagely.aiservice.ai.domain.repository;

import com.pagely.aiservice.ai.domain.model.ReportAnalysis;
import java.util.List;

public interface ReportAnalysisRepository {

    void save(ReportAnalysis analysis);

    List<ReportAnalysis> findByBookId(String bookId);
}
