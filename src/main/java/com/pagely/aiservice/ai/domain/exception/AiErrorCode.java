package com.pagely.aiservice.ai.domain.exception;

import com.pagely.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AiErrorCode implements ErrorCode {

    BOOK_PROFILE_TEXT_NOT_GENERATED(
            "BOOK_PROFILE_TEXT_NOT_GENERATED",
            "도서 프로필 텍스트가 생성되지 않았습니다.",
            HttpStatus.INTERNAL_SERVER_ERROR),

    OPENAI_KEYWORD_PARSING_FAILED(
            "OPENAI_KEYWORD_PARSING_FAILED",
            "OPEN AI에서 키워드 파싱에 실패하였습니다.",
            HttpStatus.INTERNAL_SERVER_ERROR),

    USER_PROFILE_NOT_FOUND(
            "USER_PROFILE_NOT_FOUND",
            "해당 유저의 프로필 텍스트가 존재하지 않습니다.",
            HttpStatus.NOT_FOUND);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
