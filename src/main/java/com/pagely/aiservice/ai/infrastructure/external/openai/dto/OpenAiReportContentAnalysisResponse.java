package com.pagely.aiservice.ai.infrastructure.external.openai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record OpenAiReportContentAnalysisResponse(
        @JsonProperty("usedKeywords")    List<String> usedKeywords,
        @JsonProperty("refinedKeywords") List<String> refinedKeywords,
        @JsonProperty("summarized")      String summarized,
        @JsonProperty("sentiment")       String sentiment
) {}
