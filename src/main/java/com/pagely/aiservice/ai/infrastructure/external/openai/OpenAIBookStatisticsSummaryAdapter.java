package com.pagely.aiservice.ai.infrastructure.external.openai;

import com.pagely.aiservice.ai.application.port.out.BookStatisticsSummaryPort;
import java.util.Map;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class OpenAIBookStatisticsSummaryAdapter implements BookStatisticsSummaryPort {

    private final ChatClient chatClient;

    public OpenAIBookStatisticsSummaryAdapter(
            @Qualifier("bookStatisticsChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @Override
    public String summarize(int totalReviews,
                            Map<String, Double> sentimentDistribution,
                            Map<String, Long> topKeywords) {
        String prompt = """
                아래 독자 반응 데이터를 자연스러운 한국어 문장 1~2개로 요약해주세요.
                
                - 총 독후감 수: %d건
                - 감성 분포: %s
                - 주요 키워드: %s
                
                예시: "총 32명의 독자 중 78%%가 긍정적인 반응을 보였으며, 감동적이고 여운이 남는다는 키워드가 주를 이뤘습니다."
                """.formatted(totalReviews, sentimentDistribution, topKeywords.keySet());

        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }
}
