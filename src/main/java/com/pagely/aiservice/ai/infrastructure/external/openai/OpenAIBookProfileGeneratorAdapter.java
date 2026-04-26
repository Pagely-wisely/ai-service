package com.pagely.aiservice.ai.infrastructure.external.openai;

import com.pagely.aiservice.ai.application.port.out.BookProfileGeneratorPort;
import com.pagely.aiservice.ai.domain.model.ReportAnalysis;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class OpenAIBookProfileGeneratorAdapter implements BookProfileGeneratorPort {

    private final ChatClient chatClient;

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
        return callApi(bookId, title, author, category, description, analysisList);
    }

    private String callApi(String bookId,
                           String title,
                           String author,
                           String category,
                           String description,
                           List<ReportAnalysis> analysisList) {
        return chatClient.prompt()
                .system("""
                        당신은 도서 추천 시스템을 위한 도서 프로필 텍스트 생성 전문가입니다.
                        아래 도서 정보와 독자 반응 데이터를 바탕으로 임베딩 벡터 생성에 최적화된 프로필 텍스트를 작성해주세요.
                        
                        [작성 규칙]
                        - 자연어 서술형으로 작성 (JSON, 마크다운, 특수기호 금지)
                        - 200~300자 내외
                        - 도서의 특징과 감성을 풍부하게 표현
                        - 독후감 데이터가 있다면 독자 반응도 자연스럽게 녹여낼 것
                        - 임베딩 검색에 유리하도록 핵심 키워드를 자연스럽게 포함할 것
                        """)
                .user(buildUserPrompt(bookId, title, author, category, description, analysisList))
                .call()
                .content();
    }

    private String buildUserPrompt(String bookId,
                                   String title,
                                   String author,
                                   String category,
                                   String description,
                                   List<ReportAnalysis> analysisList) {
        StringBuilder sb = new StringBuilder();

        sb.append("[도서 정보]\n");
        sb.append("- 제목: ").append(title).append("\n");
        sb.append("- 저자: ").append(author).append("\n");
        sb.append("- 카테고리: ").append(category).append("\n");
        sb.append("- 도서 소개: ")
                .append(description != null ? description : "정보 없음")
                .append("\n");

        if (analysisList != null && !analysisList.isEmpty()) {
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

            sb.append("\n[독자 반응 데이터]\n");
            sb.append("- 총 독후감 수: ").append(analysisList.size()).append("건\n");
            sb.append("- 주요 키워드: ").append(String.join(", ", topKeywords)).append("\n");
            sb.append("- 감성 분포: ").append(sentimentCount).append("\n");
            sb.append("- 독후감 요약 샘플:\n");
            summaries.forEach(s -> sb.append("  · ").append(s).append("\n"));
        }

        return sb.toString();
    }
}
