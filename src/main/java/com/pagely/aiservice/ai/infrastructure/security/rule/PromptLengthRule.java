package com.pagely.aiservice.ai.infrastructure.security.rule;


import com.pagely.aiservice.ai.domain.exception.AiErrorCode;
import com.pagely.common.exception.BusinessException;
import org.springframework.stereotype.Component;

@Component
public class PromptLengthRule implements PromptValidationRule {

    private static final int MAX_INPUT_LENGTH = 3000;

    @Override
    public void validate(String input) {
        if (input == null || input.isBlank()) {
            throw new BusinessException(AiErrorCode.INVALID_INPUT);
        }
        if (input.length() > MAX_INPUT_LENGTH) {
            throw new BusinessException(AiErrorCode.INPUT_TOO_LONG);
        }
    }
}