package com.pagely.aiservice.ai.domain.repository;

import com.pagely.aiservice.ai.domain.model.Inbox;

public interface InboxRepository {

    Inbox save(Inbox inbox);

    boolean existsByEventId(String eventId);
}
