package com.pagely.aiservice.ai.infrastructure.judge;

import com.pagely.aiservice.ai.infrastructure.judge.dto.JudgeRawResponse;
import com.pagely.aiservice.ai.infrastructure.judge.dto.JudgeVerdict;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SpringAiJudgeAdapter implements JudgePort {

    private final ChatClient judgeChatClient;

    @Override
    public JudgeVerdict judge(String prompt) {
        JudgeRawResponse response = judgeChatClient.prompt()
                .user(prompt)
                .call()
                .entity(JudgeRawResponse.class);

        return JudgeVerdict.valueOf(response.verdict());
    }
}
