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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookProfileService {

    private final BookProfileGeneratorPort bookProfileGeneratorPort;
    private final BookEmbeddingPort bookEmbeddingPort;
    private final ReportAnalysisRepository reportAnalysisRepository;

    public void initiateBookProfile(BookProfileGenerateCommand dto) {
        List<ReportAnalysis> analysisList = reportAnalysisRepository.findByBookId(dto.bookId());

        String profileText = bookProfileGeneratorPort.generate(dto.bookId(), dto.title(), dto.author(), dto.category(),
                dto.description(), analysisList);

        validateProfileText(dto, profileText);

        bookEmbeddingPort.initiate(dto.bookId(), profileText, dto.title(), dto.author(), dto.category(), dto.description());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateBookProfile(String bookId,
                                  String title,
                                  String author,
                                  String category,
                                  String description) {
        String updatedProfileText = bookProfileGeneratorPort.generate(
                bookId,
                title,
                author,
                category,
                description,
                reportAnalysisRepository.findByBookId(bookId)
        );

        bookEmbeddingPort.update(
                bookId,
                updatedProfileText,
                title,
                author,
                category,
                description
        );
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
