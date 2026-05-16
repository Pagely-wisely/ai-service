package com.pagely.aiservice.ai.infrastructure.external.openai;

import com.pagely.aiservice.ai.application.dto.result.ReportContentAnalysisResult;
import com.pagely.aiservice.ai.application.port.out.ReportAnalysisPort;
import com.pagely.aiservice.ai.infrastructure.external.openai.dto.OpenAiReportContentAnalysisResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class OpenAIReportAnalysisAdapter implements ReportAnalysisPort {

    private final ChatClient chatClient;

    @Value("classpath:prompts/report-analysis-system.st")
    private Resource systemPromptResource;

    public OpenAIReportAnalysisAdapter(
            @Qualifier("reportAnalysisChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @Override
    public ReportContentAnalysisResult analyze(String reviewText) {
        OpenAiReportContentAnalysisResponse response = chatClient.prompt()
                .system(systemPromptResource)
                .user(reviewText)
                .call()
                .entity(OpenAiReportContentAnalysisResponse.class);

        return toApplicationDto(response);
    }

    private ReportContentAnalysisResult toApplicationDto(OpenAiReportContentAnalysisResponse response) {
        return new ReportContentAnalysisResult(
                response.usedKeywords(),
                response.refinedKeywords(),
                response.summarized(),
                response.sentiment()
        );
    }
}
