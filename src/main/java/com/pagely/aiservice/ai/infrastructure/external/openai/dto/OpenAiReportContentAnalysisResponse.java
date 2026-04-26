package com.pagely.aiservice.ai.infrastructure.external.openai.dto;

import java.util.List;

public record OpenAiReportContentAnalysisResponse(
        List<String> usedKeywords,
        List<String> refinedKeywords,
        String summarized,
        String sentiment
) {
    public static OpenAiReportContentAnalysisResponse empty() {
        return new OpenAiReportContentAnalysisResponse(
                List.of(), List.of(), "", "NEUTRAL"
        );
    }
}