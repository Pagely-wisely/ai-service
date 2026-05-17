package com.pagely.aiservice.ai.application.port.out;

import java.util.Map;

public interface BookStatisticsSummaryPort {
    String summarize(int totalReviews,
                     Map<String, Double> sentimentDistribution,
                     Map<String, Long> topKeywords);
}
