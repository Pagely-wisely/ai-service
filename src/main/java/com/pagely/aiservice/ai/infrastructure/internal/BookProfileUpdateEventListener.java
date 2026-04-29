package com.pagely.aiservice.ai.infrastructure.internal;

import com.pagely.aiservice.ai.application.event.ReportAnalysisCompletedEvent;
import com.pagely.aiservice.ai.application.service.BookProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class BookProfileUpdateEventListener {

    private final BookProfileService bookProfileService;

    @Async("aiInternalEventExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(ReportAnalysisCompletedEvent event) {
        bookProfileService.updateBookProfile(
                event.bookId(),
                event.title(),
                event.author(),
                event.category(),
                event.description()
        );
    }
}