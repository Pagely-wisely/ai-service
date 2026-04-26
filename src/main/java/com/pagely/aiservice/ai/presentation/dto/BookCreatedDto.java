package com.pagely.aiservice.ai.presentation.dto;

import com.pagely.aiservice.ai.application.dto.command.BookProfileGenerateCommand;

public record BookCreatedDto(
        String bookId,
        String categoryId,
        String title,
        String author,
        String description
){
    public BookProfileGenerateCommand toCommand() {
        return new BookProfileGenerateCommand(bookId, categoryId, title, author, description);
    }
}
