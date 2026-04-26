package com.pagely.aiservice.ai.infrastructure.persistence;

import com.pagely.aiservice.ai.domain.model.ReportAnalysis;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportAnalysisJpaRepository extends JpaRepository<ReportAnalysis, UUID> {

    List<ReportAnalysis> findByBookId(String bookId);
}
