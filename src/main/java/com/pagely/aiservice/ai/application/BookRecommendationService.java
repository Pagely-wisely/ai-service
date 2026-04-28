package com.pagely.aiservice.ai.application;

import com.pagely.aiservice.ai.application.port.out.BookRecommendationPort;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookRecommendationService {

    private final BookRecommendationPort bookRecommendationPort;

    public List<String> recommend(UUID userId) {
        List<String> bookIds = bookRecommendationPort.recommend(userId);
        return bookIds;
    }
}
