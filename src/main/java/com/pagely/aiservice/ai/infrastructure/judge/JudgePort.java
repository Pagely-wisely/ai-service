package com.pagely.aiservice.ai.infrastructure.judge;

import com.pagely.aiservice.ai.infrastructure.judge.dto.JudgeVerdict;

public interface JudgePort {
    JudgeVerdict judge(String prompt);
}
