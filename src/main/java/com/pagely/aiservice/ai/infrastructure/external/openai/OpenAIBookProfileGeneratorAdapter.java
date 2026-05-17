package com.pagely.aiservice.ai.infrastructure.external.openai;

import com.pagely.aiservice.ai.application.port.out.BookProfileGeneratorPort;
import com.pagely.aiservice.ai.domain.model.ReportAnalysis;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class OpenAIBookProfileGeneratorAdapter implements BookProfileGeneratorPort {

    private final ChatClient chatClient;

    @Value("classpath:prompts/book-profile-system.st")
    private Resource systemPromptResource;

    @Value("classpath:prompts/book-profile-user.st")
    private Resource userPromptResource;

    public OpenAIBookProfileGeneratorAdapter(
            @Qualifier("bookProfileChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @Override
    public String generate(String bookId,
                           String title,
                           String author,
                           String category,
                           String description,
                           List<ReportAnalysis> analysisList) {
        String systemPrompt = new PromptTemplate(systemPromptResource)
                .render();

        String userPrompt = new PromptTemplate(userPromptResource)
                .render(Map.of(
                        "title", title,
                        "author", author,
                        "category", category,
                        "description", Objects.requireNonNullElse(description, "정보 없음"),
                        "readerSection", buildReaderSection(analysisList)
                ));

        return chatClient.prompt()
                .system(systemPrompt)
                .user(userPrompt)
                .call()
                .content();
    }

    private String buildReaderSection(List<ReportAnalysis> analysisList) {
        if (analysisList == null || analysisList.isEmpty()) {
            return "";
        }

        List<String> topKeywords = analysisList.stream()
                .filter(a -> a.getKeywordNormalized() != null)
                .flatMap(a -> a.getKeywordNormalized().stream())
                .collect(Collectors.groupingBy(k -> k, Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(10)
                .map(Map.Entry::getKey)
                .toList();

        Map<String, Long> sentimentCount = analysisList.stream()
                .filter(a -> a.getSentiment() != null)
                .collect(Collectors.groupingBy(ReportAnalysis::getSentiment, Collectors.counting()));

        List<String> summaries = analysisList.stream()
                .map(ReportAnalysis::getSummaryText)
                .filter(Objects::nonNull)
                .limit(5)
                .toList();

        StringBuilder sb = new StringBuilder("\n[독자 반응 데이터]\n");
        sb.append("- 총 독후감 수: ").append(analysisList.size()).append("건\n");
        sb.append("- 주요 키워드: ").append(String.join(", ", topKeywords)).append("\n");
        sb.append("- 감성 분포: ").append(sentimentCount).append("\n");
        sb.append("- 독후감 요약 샘플:\n");
        summaries.forEach(s -> sb.append("  · ").append(s).append("\n"));

        return sb.toString();
    }
}
