package com.pagely.aiservice.ai.presentation;

import com.pagely.aiservice.ai.application.service.BookRecommendationService;
import com.pagely.aiservice.ai.application.service.BookReportStatisticsService;
import com.pagely.common.response.ApiResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiResponse> getReportStatistics(@PathVariable("book_id") String bookId) {
        return ApiResponse.ok(bookReportStatisticsService.execute(bookId));
    }

    @GetMapping("/recommend/book/{user_id}")
    public ResponseEntity<ApiResponse> recommendBook(@PathVariable("user_id") UUID userId) {
        return ApiResponse.ok(bookRecommendationService.recommend(userId));
    }
}
