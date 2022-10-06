package com.woony.core.domain.request;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Value;

@Getter
@Value
public class UpdateDateRequestWithoutString {
    //JsonFormat: 자체적으로 변환해준다.
    @DateTimeFormat
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy hh:mm:ss a", timezone = "Asia/Seoul")
    private LocalDateTime dateWithLDT;
}
