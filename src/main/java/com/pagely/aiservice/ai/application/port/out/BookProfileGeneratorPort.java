package com.pagely.aiservice.ai.application.port.out;

import com.pagely.aiservice.ai.domain.model.ReportAnalysis;
import java.util.List;

public interface BookProfileGeneratorPort {

    public String generate(String bookId,
                           String title,
                           String author,
                           String category,
                           String description,
                           List<ReportAnalysis> analysisList);
}
