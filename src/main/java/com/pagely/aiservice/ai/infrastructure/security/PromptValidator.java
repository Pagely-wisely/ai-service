package com.pagely.aiservice.ai.infrastructure.security;

import com.pagely.aiservice.ai.infrastructure.security.rule.PromptValidationRule;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class PromptValidator {

    private final List<PromptValidationRule> rules;

    public PromptValidator(List<PromptValidationRule> rules) {
        this.rules = rules;
    }

    public String validate(String input) {
        rules.forEach(rule -> rule.validate(input));
        return input.strip();
    }
}
