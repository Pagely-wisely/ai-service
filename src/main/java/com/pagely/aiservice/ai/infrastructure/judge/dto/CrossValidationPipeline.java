package com.pagely.aiservice.ai.infrastructure.judge.dto;

import com.pagely.aiservice.ai.infrastructure.judge.JudgePort;
import com.pagely.aiservice.ai.infrastructure.judge.JudgePromptBuilder;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CrossValidationPipeline {

    private final JudgePort judgePort;
    private final JudgePromptBuilder promptBuilder;

    public JudgeVerdict execute(JudgeRequest request) {
        List<JudgeVerdict> results = new ArrayList<>();

        for (int round = 1; round <= request.rounds(); round++) {
            String prompt = promptBuilder.build(request, round);
            JudgeVerdict verdict = judgePort.judge(prompt);
            results.add(verdict);

            if (results.size() >= 2) {
                JudgeVerdict prev = results.get(results.size() - 2);
                JudgeVerdict curr = results.get(results.size() - 1);
                if (prev == curr) {
                    break;
                }
            }
        }

        return aggregate(results);
    }

    private JudgeVerdict aggregate(List<JudgeVerdict> results) {
        long passCount = results.stream().filter(v -> v == JudgeVerdict.PASS).count();
        return passCount > results.size() / 2 ? JudgeVerdict.PASS : JudgeVerdict.FAIL;
    }
}
