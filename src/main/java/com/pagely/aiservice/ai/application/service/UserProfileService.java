package com.pagely.aiservice.ai.application.service;

import com.pagely.aiservice.ai.application.port.UserEmbeddingPort;
import com.pagely.aiservice.ai.application.port.UserProfileGeneratorPort;
import com.pagely.aiservice.ai.domain.model.ReportAnalysis;
import com.pagely.aiservice.ai.domain.model.UserLikedBook;
import com.pagely.aiservice.ai.domain.model.UserSearchHistory;
import com.pagely.aiservice.ai.domain.repository.ReportAnalysisRepository;
import com.pagely.aiservice.ai.domain.repository.UserLikedBookRepository;
import com.pagely.aiservice.ai.domain.repository.UserSearchHistoryRepository;
import com.pagely.aiservice.ai.presentation.dto.PersonaCreateDto;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserProfileGeneratorPort userProfileGeneratorPort;
    private final UserEmbeddingPort userEmbeddingPort;

    private final UserLikedBookRepository userLikedBookRepository;
    private final UserSearchHistoryRepository userSearchHistoryRepository;
    private final ReportAnalysisRepository reportAnalysisRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void generateUserProfile(UUID userId) {
        List<UserLikedBook> likedBooks = userLikedBookRepository.findByUserId(userId);
        List<UserSearchHistory> userSearchHistories = userSearchHistoryRepository.findByUserId(userId);
        List<ReportAnalysis> reportAnalyses = reportAnalysisRepository.findByUserId(userId);
        String userProfileText = userProfileGeneratorPort.generate(likedBooks, userSearchHistories, reportAnalyses);
        userEmbeddingPort.update(userId, userProfileText);
    }

    public void generatePersonaProfile(PersonaCreateDto dto) {
        userEmbeddingPort.initialize(dto.userId(), dto.profileText());
    }
}
