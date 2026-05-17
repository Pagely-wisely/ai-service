package com.pagely.aiservice.ai.infrastructure.persistence;

import com.pagely.aiservice.ai.domain.model.Inbox;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InboxJpaRepository extends JpaRepository<Inbox, String> {

    boolean existsByEventId(String eventId);
}
