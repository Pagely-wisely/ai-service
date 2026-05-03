package com.pagely.aiservice.ai.application.service;

import com.pagely.aiservice.ai.application.dto.result.BookReaderInsightsResult;
import com.pagely.aiservice.ai.application.port.out.BookStatisticsSummaryPort;
import com.pagely.aiservice.ai.domain.model.ReportAnalysis;
import com.pagely.aiservice.ai.domain.repository.ReportAnalysisRepository;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class BookReportStatisticsService {

    private final ReportAnalysisRepository reportAnalysisRepository;
    private final BookStatisticsSummaryPort bookStatisticsSummaryPort;

    public BookReportStatisticsService(
            ReportAnalysisRepository reportAnalysisRepository,
            BookStatisticsSummaryPort bookStatisticsSummaryPort) {
        this.reportAnalysisRepository = reportAnalysisRepository;
        this.bookStatisticsSummaryPort = bookStatisticsSummaryPort;
    }

    public BookReaderInsightsResult execute(String bookId) {
        List<ReportAnalysis> analysisList =
                reportAnalysisRepository.findByBookId(bookId);

        Map<String, Double> sentimentDistribution = analysisList.stream()
                .collect(Collectors.groupingBy(
                        ReportAnalysis::getSentiment,
                        Collectors.collectingAndThen(
                                Collectors.counting(),
                                count -> (double) count / analysisList.size() * 100
                        )
                ));

        Map<String, Long> topKeywords = analysisList.stream()
                .filter(a -> a.getKeywordNormalized() != null)
                .flatMap(a -> a.getKeywordNormalized().stream())
                .filter(k -> k != null && !k.isBlank())
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

        String summary = bookStatisticsSummaryPort.summarize(
                analysisList.size(),
                sentimentDistribution,
                topKeywords
        );

        return new BookReaderInsightsResult(
                bookId,
                analysisList.size(),
                sentimentDistribution,
                topKeywords,
                summary
        );
    }
}
