package com.pagely.aiservice.ai.presentation;

import com.pagely.aiservice.ai.application.BookRecommendationService;
import com.pagely.aiservice.ai.application.dto.BookReaderInsightsResult;
import com.pagely.aiservice.ai.application.service.BookReportStatisticsService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ai")
@RequiredArgsConstructor
public class AiController {

    private final BookReportStatisticsService bookReportStatisticsService;
    private final BookRecommendationService bookRecommendationService;

    @GetMapping("/{book_id}/report-statistics")
    public BookReaderInsightsResult getReportStatistics(@PathVariable("book_id") String bookId) {
        return bookReportStatisticsService.execute(bookId);
    }

    @GetMapping("/recommend/book/{user_id}")
    public List<String> recommendBook(@PathVariable("user_id") UUID userId) {
        return bookRecommendationService.recommend(userId);
    }
}
