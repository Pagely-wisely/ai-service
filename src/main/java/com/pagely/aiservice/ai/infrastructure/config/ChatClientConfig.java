package com.pagely.aiservice.ai.infrastructure.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {

    @Bean
    @Qualifier("reportAnalysisChatClient")
    public ChatClient reportAnalysisChatClient(ChatClient.Builder builder) {
        return builder
                .defaultOptions(OpenAiChatOptions.builder()
                        .model("gpt-4o-mini")
                        .temperature(0.0)
                        .build())
                .build();
    }

    @Bean
    @Qualifier("bookProfileChatClient")
    public ChatClient bookProfileChatClient(ChatClient.Builder builder) {
        return builder
                .defaultOptions(OpenAiChatOptions.builder()
                        .model("gpt-4o-mini")
                        .temperature(0.7)
                        .build())
                .build();
    }

    @Bean
    @Qualifier("userProfileChatClient")
    public ChatClient userProfileChatClient(ChatClient.Builder builder) {
        return builder
                .defaultOptions(OpenAiChatOptions.builder()
                        .model("gpt-4o-mini")  // 도서보다 단순한 요약 → mini로 비용 절감
                        .temperature(0.3)       // 일관된 프로필 생성, 너무 창의적일 필요 없음
                        .build())
                .build();
    }
}
