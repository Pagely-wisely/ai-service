package com.pagely.aiservice.ai.infrastructure.external.openai;

import com.pagely.aiservice.ai.application.port.out.BookStatisticsSummaryPort;
import java.util.Map;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class OpenAIBookStatisticsSummaryAdapter implements BookStatisticsSummaryPort {

    private final ChatClient chatClient;

    @Value("classpath:prompts/book-statistics-summary-user.st")
    private Resource userPromptResource;

    public OpenAIBookStatisticsSummaryAdapter(
            @Qualifier("bookStatisticsChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @Override
    public String summarize(int totalReviews,
                            Map<String, Double> sentimentDistribution,
                            Map<String, Long> topKeywords) {
        String userPrompt = new PromptTemplate(userPromptResource)
                .render(Map.of(
                        "totalReviews",          totalReviews,
                        "sentimentDistribution", sentimentDistribution,
                        "topKeywords",           topKeywords.keySet()
                ));

        return chatClient.prompt()
                .user(userPrompt)
                .call()
                .content();
    }
}
