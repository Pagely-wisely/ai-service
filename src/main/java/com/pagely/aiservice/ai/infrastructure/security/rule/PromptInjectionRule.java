package com.pagely.aiservice.ai.infrastructure.security.rule;


import com.pagely.aiservice.ai.domain.exception.AiErrorCode;
import com.pagely.common.exception.BusinessException;
import java.util.List;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class PromptInjectionRule implements PromptValidationRule {

    private static final List<Pattern> INJECTION_PATTERNS = List.of(
            Pattern.compile("(?i)(ignore|forget|disregard).*(above|previous|instruction|system)", Pattern.DOTALL),
            Pattern.compile("(?i)(system|user|assistant)\\s*prompt"),
            Pattern.compile("(?i)you\\s*are\\s*now"),
            Pattern.compile("(?i)new\\s*(role|instruction|persona)"),
            Pattern.compile("(?i)\\[system]|<system>|</system>"),
            Pattern.compile("(?i)act\\s*as\\s*(a|an)\\s+\\w+")
    );

    @Override
    public void validate(String input) {
        for (Pattern pattern : INJECTION_PATTERNS) {
            if (pattern.matcher(input).find()) {
                throw new BusinessException(AiErrorCode.PROMPT_INJECTION_DETECTED);
            }
        }
    }
}
