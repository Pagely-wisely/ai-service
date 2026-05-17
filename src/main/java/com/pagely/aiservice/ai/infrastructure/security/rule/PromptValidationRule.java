package com.pagely.aiservice.ai.infrastructure.security.rule;

public interface PromptValidationRule {
    void validate(String prompt);
}
