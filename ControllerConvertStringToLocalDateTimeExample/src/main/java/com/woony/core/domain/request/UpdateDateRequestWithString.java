package com.woony.core.domain.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateDateRequestWithString {
    private String dateAsString;

    @Builder
    public UpdateDateRequestWithString(String dateAsString) {
        this.dateAsString = dateAsString;
    }
}
