package com.pagely.aiservice.ai.infrastructure.security;

import com.pagely.aiservice.ai.infrastructure.security.rule.PromptValidationRule;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class PromptValidator implements PromptValidationRule {

    private final List<PromptValidationRule> rules;

    public PromptValidator(List<PromptValidationRule> rules) {
        this.rules = rules;
    }

    public void validate(String input) {
        rules.forEach(rule -> rule.validate(input));
    }
}
