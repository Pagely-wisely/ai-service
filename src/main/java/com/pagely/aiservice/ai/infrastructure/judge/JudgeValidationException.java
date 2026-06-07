package com.pagely.aiservice.ai.infrastructure.judge;

import com.pagely.aiservice.ai.infrastructure.judge.dto.JudgeVerdict;

public class JudgeValidationException extends RuntimeException {

    private final String query;
    private final String response;
    private final JudgeVerdict verdict;

    public JudgeValidationException(String query, String response, JudgeVerdict verdict) {
        super("Judge 검증 실패 — verdict: " + verdict);
        this.query = query;
        this.response = response;
        this.verdict = verdict;
    }
}
