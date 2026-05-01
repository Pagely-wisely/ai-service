package com.pagely.aiservice.ai.presentation;

import com.pagely.aiservice.ai.application.dto.command.BookLikedCommand;
import com.pagely.aiservice.ai.application.dto.command.BookSearchedCommand;
import com.pagely.aiservice.ai.application.service.BookCreatedService;
import com.pagely.aiservice.ai.application.service.ReportAnalysisService;
import com.pagely.aiservice.ai.application.service.UserActionService;
import com.pagely.aiservice.ai.application.service.UserProfileService;
import com.pagely.aiservice.ai.presentation.dto.BookCreatedDto;
import com.pagely.aiservice.ai.presentation.dto.PersonaCreateDto;
import com.pagely.aiservice.ai.presentation.dto.ReportCreatedDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tmp")
@RequiredArgsConstructor
public class TmpEventController {

    private final ReportAnalysisService reportCreatedService;
    private final BookCreatedService bookCreatedService;
    private final UserActionService userActionService;
    private final UserProfileService userProfileService;

    @GetMapping
    public String handleReportCreated(@ModelAttribute ReportCreatedDto dto) {
        reportCreatedService.handleReportCreated(dto.toCommand());
        return "success";
    }

    @GetMapping("/book")
    public String handleBookCreated(@ModelAttribute BookCreatedDto dto) {
        bookCreatedService.handleBookCreated(dto.toCommand());
        return "success";
    }

    @PostMapping("/user/bookLike")
    public String handleUserBookLiked(@RequestBody BookLikedCommand command) {
        userActionService.handleBookLiked(command);
        return "success";
    }

    @PostMapping("/user/bookSearched")
    public String handleUserBookSearched(@RequestBody BookSearchedCommand command) {
        userActionService.handleBookSearched(command);
        return "success";
    }

    @PostMapping("/user/persona")
    public void generatePersonaProfile(@RequestBody PersonaCreateDto dto) {
        userProfileService.generatePersonaProfile(dto);
    }
}
