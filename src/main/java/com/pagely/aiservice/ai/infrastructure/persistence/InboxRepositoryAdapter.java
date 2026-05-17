package com.pagely.aiservice.ai.infrastructure.persistence;

import com.pagely.aiservice.ai.domain.model.Inbox;
import com.pagely.aiservice.ai.domain.repository.InboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InboxRepositoryAdapter implements InboxRepository {

    private final InboxJpaRepository inboxJpaRepository;

    @Override
    public Inbox save(Inbox inbox) {
        return inboxJpaRepository.save(inbox);
    }

    @Override
    public boolean existsByEventId(String eventId) {
        return inboxJpaRepository.existsByEventId(eventId);
    }
}
