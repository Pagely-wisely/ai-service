package com.pagely.aiservice.ai.presentation;

import com.pagely.aiservice.ai.application.service.ReportAnalysisService;
import com.pagely.aiservice.ai.presentation.dto.BookCreatedDto;
import com.pagely.aiservice.ai.presentation.dto.ReportCreatedDto;
import com.pagely.aiservice.ai.application.service.BookCreatedService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tmp")
@RequiredArgsConstructor
public class TmpEventController {

    private final ReportAnalysisService reportCreatedService;
    private final BookCreatedService bookCreatedService;

    @GetMapping
    public String handleReportCreated(@ModelAttribute ReportCreatedDto dto){
        reportCreatedService.handleReportCreated(dto.toCommand());
        return "success";
    }

    @GetMapping("/book")
    public String handleBookCreated(@ModelAttribute BookCreatedDto dto){
        bookCreatedService.handleBookCreated(dto.toCommand());
        return "success";
    }
}
