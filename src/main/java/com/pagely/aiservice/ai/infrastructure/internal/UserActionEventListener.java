package com.pagely.aiservice.ai.infrastructure.internal;

import com.pagely.aiservice.ai.application.service.UserProfileService;
import com.pagely.aiservice.ai.application.service.UserProfileTextUpdateEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class UserActionEventListener {

    private final UserProfileService userProfileService;

    @Async("aiInternalEventExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleBookLikedStored(UserProfileTextUpdateEvent event) {
        userProfileService.generateUserProfile(event.userId());
    }
}
