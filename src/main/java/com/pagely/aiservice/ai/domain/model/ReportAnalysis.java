package com.pagely.aiservice.ai.domain.model;

import com.pagely.aiservice.ai.common.BaseEntity;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UuidGenerator;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "p_report_analysis")
public class ReportAnalysis extends BaseEntity {

    @Id
    @UuidGenerator
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false)
    private UUID reportId;

    private String bookId;

    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    private List<String> keywordOriginal;

    @Type(JsonBinaryType.class)
    @Column(columnDefinition = "jsonb")
    private List<String> keywordNormalized;

    private String sentiment;

    @Column(columnDefinition = "TEXT")
    private String summaryText;

    @Builder
    private ReportAnalysis(UUID reportId,
                           String bookId,
                           List<String> keywordOriginal,
                           List<String> keywordNormalized,
                           String sentiment,
                           String summaryText) {
        this.reportId = reportId;
        this.bookId = bookId;
        this.keywordOriginal = keywordOriginal;
        this.keywordNormalized = keywordNormalized;
        this.sentiment = sentiment;
        this.summaryText = summaryText;
    }
}