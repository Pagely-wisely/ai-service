package com.pagely.aiservice.ai.infrastructure.external.openai;

import com.pagely.aiservice.ai.application.port.UserProfileGeneratorPort;
import com.pagely.aiservice.ai.domain.model.ReportAnalysis;
import com.pagely.aiservice.ai.domain.model.UserLikedBook;
import com.pagely.aiservice.ai.domain.model.UserSearchHistory;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class OpenAIUserProfileGeneratorAdapter implements UserProfileGeneratorPort {

    private final ChatClient chatClient;

    @Value("classpath:prompts/user-profile-system.st")
    private Resource systemPromptResource;

    @Value("classpath:prompts/user-profile-user.st")
    private Resource userPromptResource;

    public OpenAIUserProfileGeneratorAdapter(
            @Qualifier("userProfileChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @Override
    public String generate(
            List<UserLikedBook> likedBooks,
            List<UserSearchHistory> searchHistories,
            List<ReportAnalysis> reportAnalyses
    ) {
        String userPrompt = new PromptTemplate(userPromptResource)
                .render(Map.of(
                        "reportSection", buildReportSection(reportAnalyses),
                        "likedSection",  buildLikedSection(likedBooks),
                        "searchSection", buildSearchSection(searchHistories)
                ));

        return chatClient.prompt()
                .system(systemPromptResource)
                .user(userPrompt)
                .call()
                .content();
    }

    private String buildReportSection(List<ReportAnalysis> reportAnalyses) {
        if (!hasData(reportAnalyses)) {
            return "";
        }

        List<String> topKeywords = reportAnalyses.stream()
                .filter(a -> a.getKeywordNormalized() != null)
                .flatMap(a -> a.getKeywordNormalized().stream())
                .collect(Collectors.groupingBy(k -> k, Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(10)
                .map(Map.Entry::getKey)
                .toList();

        Map<String, Long> sentimentCount = reportAnalyses.stream()
                .filter(a -> a.getSentiment() != null)
                .collect(Collectors.groupingBy(ReportAnalysis::getSentiment, Collectors.counting()));

        return "\n[독후감 작성 도서 - 높은 관심]\n" +
                "- 총 독후감 수: " + reportAnalyses.size() + "건\n" +
                "- 주요 키워드: " + String.join(", ", topKeywords) + "\n" +
                "- 감성 분포: " + sentimentCount + "\n";
    }

    private String buildLikedSection(List<UserLikedBook> likedBooks) {
        if (!hasData(likedBooks)) {
            return "";
        }

        StringBuilder sb = new StringBuilder("\n[좋아요한 도서 - 중간 관심]\n");
        likedBooks.forEach(b ->
                sb.append("- ").append(b.getTitle())
                        .append(" / ").append(b.getAuthor())
                        .append(" (").append(b.getCategory()).append(")\n"));
        return sb.toString();
    }

    private String buildSearchSection(List<UserSearchHistory> searchHistories) {
        if (!hasData(searchHistories)) {
            return "";
        }

        StringBuilder sb = new StringBuilder("\n[검색한 도서 - 낮은 관심]\n");
        searchHistories.stream()
                .limit(20)
                .forEach(s -> {
                    if (s.getTitle() != null) {
                        sb.append("- ").append(s.getTitle())
                                .append(" (").append(s.getCategory()).append(")\n");
                    } else if (s.getSearchKeyword() != null) {
                        sb.append("- 검색어: ").append(s.getSearchKeyword()).append("\n");
                    }
                });
        return sb.toString();
    }

    private boolean hasData(List<?> list) {
        return list != null && !list.isEmpty();
    }
}
