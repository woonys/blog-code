package com.woony.core.controller;

import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.woony.core.domain.request.UpdateDateRequestWithString;
import com.woony.core.domain.request.UpdateDateRequestWithoutString;
import com.woony.core.service.UpdateDateService;

@RestController
@RequestMapping("/core")
public class UpdateDateController {

    private UpdateDateService updateDateService;

    public UpdateDateController(UpdateDateService updateDateService) {
        this.updateDateService = updateDateService;
    }
    // Case 1: String -> Date 변환 로직 포함한 기능 구현
    @PutMapping("/updateDateWithString/{dateId}")
    public String updateDateFromString(@PathVariable long dateId, @RequestBody UpdateDateRequestWithString request) {
        String fetchDate = request.getDateAsString();
        updateDateService.updateCurrentDateWithString(dateId, fetchDate);
        return "Success";
    }

    @PutMapping("/updateDateWithoutString/{dateId}")
    public String updateDateWithoutString(@PathVariable long dateId, @RequestBody UpdateDateRequestWithoutString request) {
        LocalDateTime fetchDate = request.getDateWithLDT();
        updateDateService.updateCurrentDateWithoutString(dateId, fetchDate);
        return "Success";
    }
}
