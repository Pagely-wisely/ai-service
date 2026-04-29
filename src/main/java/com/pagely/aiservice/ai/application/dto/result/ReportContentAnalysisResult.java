package com.pagely.aiservice.ai.application.dto.result;

import java.util.List;

public record ReportContentAnalysisResult(
        List<String> originals,
        List<String> renormalized,
        String summarized,
        String sentiment
) {
}
