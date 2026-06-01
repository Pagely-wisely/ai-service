package com.pagely.aiservice.ai.infrastructure.judge;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JudgeValidated {
    int rounds() default 2;
    String systemPromptPath();
}
