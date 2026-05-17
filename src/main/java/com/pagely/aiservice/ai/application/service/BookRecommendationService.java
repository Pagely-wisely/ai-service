package com.pagely.aiservice.ai.application.service;

import com.pagely.aiservice.ai.application.dto.result.BookRecommendationResult;
import com.pagely.aiservice.ai.application.port.out.BookRecommendationPort;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookRecommendationService {

    private final BookRecommendationPort bookRecommendationPort;

    public List<BookRecommendationResult> recommend(UUID userId) {
        return bookRecommendationPort.recommend(userId);
    }
}
