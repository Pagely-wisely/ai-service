package com.pagely.aiservice.ai.application.service;

import com.pagely.aiservice.ai.application.dto.BookReaderInsightsResult;
import com.pagely.aiservice.ai.domain.model.ReportAnalysis;
import com.pagely.aiservice.ai.domain.repository.ReportAnalysisRepository;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookReportStatisticsService {

    private final ReportAnalysisRepository reportAnalysisRepository;

    public BookReaderInsightsResult execute(String bookId) {
        List<ReportAnalysis> analysisList =
                reportAnalysisRepository.findByBookId(bookId);

        // 감성 비율 계산
        Map<String, Double> sentimentDistribution = analysisList.stream()
                .collect(Collectors.groupingBy(
                        ReportAnalysis::getSentiment,
                        Collectors.collectingAndThen(
                                Collectors.counting(),
                                count -> (double) count / analysisList.size() * 100
                        )
                ));

        // 상위 키워드 추출
        Map<String, Long> topKeywords = analysisList.stream()
                .flatMap(a -> a.getKeywordNormalized().stream())
                .collect(Collectors.groupingBy(k -> k, Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(10)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));

        return new BookReaderInsightsResult(
                bookId,
                analysisList.size(),
                sentimentDistribution,
                topKeywords
        );
    }
}
