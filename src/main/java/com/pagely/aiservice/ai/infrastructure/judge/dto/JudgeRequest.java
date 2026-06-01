package com.pagely.aiservice.ai.infrastructure.judge.dto;

import org.springframework.util.Assert;

public record JudgeRequest(
        String query,
        String response,
        String systemPrompt,
        int rounds
) {
    public JudgeRequest {
        Assert.hasText(query, "query는 필수입니다");
        Assert.hasText(response, "response는 필수입니다");
        Assert.hasText(systemPrompt, "systemPrompt는 필수입니다");
        Assert.isTrue(rounds >= 2 && rounds <= 3, "rounds는 2~3만 허용합니다");
    }
}
