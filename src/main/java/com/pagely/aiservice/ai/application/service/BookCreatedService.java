package com.pagely.aiservice.ai.application.service;

import com.pagely.aiservice.ai.application.dto.command.BookProfileGenerateCommand;
import com.pagely.aiservice.ai.application.port.out.BookEmbeddingPort;
import com.pagely.aiservice.ai.application.port.out.BookProfileGeneratorPort;
import com.pagely.aiservice.ai.domain.exception.AiErrorCode;
import com.pagely.aiservice.ai.domain.model.ReportAnalysis;
import com.pagely.aiservice.ai.domain.repository.ReportAnalysisRepository;
import com.pagely.common.exception.BusinessException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookCreatedService {
    private final ReportAnalysisRepository reportAnalysisRepository;

    private final BookProfileGeneratorPort bookProfileGeneratorPort;
    private final BookEmbeddingPort bookEmbeddingPort;

    public void handleBookCreated(BookProfileGenerateCommand dto) {
        List<ReportAnalysis> analysisList = reportAnalysisRepository.findByBookId(dto.bookId());

        String profileText = bookProfileGeneratorPort.generate(dto.bookId(), dto.title(), dto.author(), dto.category(),
                dto.description(), analysisList);

        validateProfileText(dto, profileText);

        bookEmbeddingPort.embedBookProfileText(dto.bookId(), profileText);
    }

    private static void validateProfileText(BookProfileGenerateCommand dto, String profileText) {
        if (isEmptyProfileText(profileText)) {
            throw new BusinessException(AiErrorCode.BOOK_PROFILE_TEXT_NOT_GENERATED, "[BOOK] " + dto.bookId());
        }
    }

    private static boolean isEmptyProfileText(String profileText) {
        return profileText == null || profileText.isBlank();
    }

}
