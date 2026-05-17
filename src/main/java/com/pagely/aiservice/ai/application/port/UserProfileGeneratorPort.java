package com.pagely.aiservice.ai.application.port;

import com.pagely.aiservice.ai.domain.model.UserLikedBook;
import com.pagely.aiservice.ai.domain.model.UserSearchHistory;
import com.pagely.aiservice.ai.domain.model.ReportAnalysis;
import java.util.List;

public interface UserProfileGeneratorPort {

    String generate(
            List<UserLikedBook> likedBooks,
            List<UserSearchHistory> searchHistories,
            List<ReportAnalysis> reportAnalyses
    );
}
