package com.pagely.aiservice.ai.infrastructure.external.openai;

import com.pagely.aiservice.ai.application.port.UserProfileGeneratorPort;
import com.pagely.aiservice.ai.domain.model.UserLikedBook;
import com.pagely.aiservice.ai.domain.model.UserSearchHistory;
import com.pagely.aiservice.ai.domain.model.ReportAnalysis;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class OpenAIUserProfileGeneratorAdapter implements UserProfileGeneratorPort {

    private final ChatClient chatClient;

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
        return chatClient.prompt()
                .system("""
                        당신은 도서 추천 시스템을 위한 독자 프로필 텍스트 생성 전문가입니다.
                        아래 독자의 독서 활동 데이터를 바탕으로 임베딩 벡터 생성에 최적화된 프로필 텍스트를 작성해주세요.
                        
                        [작성 규칙]
                        - 자연어 서술형으로 작성 (JSON, 마크다운, 특수기호 금지)
                        - 200~300자 내외 영어로 작성
                        - 독자의 취향, 관심 주제, 선호 감성을 풍부하게 표현
                        - 임베딩 검색에 유리하도록 핵심 키워드를 자연스럽게 포함할 것
                        - 활동 데이터가 적을 경우 선호 카테고리 위주로 작성
                        """)
                .user(buildUserPrompt(likedBooks, searchHistories, reportAnalyses))
                .call()
                .content();
    }

    private String buildUserPrompt(
            List<UserLikedBook> likedBooks,
            List<UserSearchHistory> searchHistories,
            List<ReportAnalysis> reportAnalyses
    ) {
        StringBuilder sb = new StringBuilder();

        // 독후감 작성 도서 (가중치 높음)
        if (hasData(reportAnalyses)) {
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

            sb.append("\n[독후감 작성 도서 - 높은 관심]\n");
            sb.append("- 총 독후감 수: ").append(reportAnalyses.size()).append("건\n");
            sb.append("- 주요 키워드: ").append(String.join(", ", topKeywords)).append("\n");
            sb.append("- 감성 분포: ").append(sentimentCount).append("\n");
        }

        // 좋아요한 도서 (가중치 중간)
        if (hasData(likedBooks)) {
            sb.append("\n[좋아요한 도서 - 중간 관심]\n");
            likedBooks.forEach(b ->
                    sb.append("- ").append(b.getTitle())
                            .append(" / ").append(b.getAuthor())
                            .append(" (").append(b.getCategory()).append(")\n"));
        }

        // 검색 기록 (가중치 낮음, 최근 20건만)
        if (hasData(searchHistories)) {
            sb.append("\n[검색한 도서 - 낮은 관심]\n");
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
        }

        return sb.toString();
    }

    private boolean hasData(List<?> list) {
        return list != null && !list.isEmpty();
    }
}
