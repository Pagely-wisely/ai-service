package com.pagely.aiservice.ai.domain.model;

import com.pagely.aiservice.ai.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "p_report_keyword")
public class ReportKeyword extends BaseEntity {

    @Id
    @UuidGenerator
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false)
    private UUID reportId;

    private String keywordOriginal;

    private String keywordNormalized;
}
