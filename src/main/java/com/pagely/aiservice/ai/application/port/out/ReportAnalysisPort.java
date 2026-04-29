package com.pagely.aiservice.ai.application.port.out;

import com.pagely.aiservice.ai.application.dto.result.ReportContentAnalysisResult;

public interface ReportAnalysisPort {

    ReportContentAnalysisResult analyze(String reportContent);
}
