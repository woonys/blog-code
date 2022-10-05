package com.woony.core.domain.request;

import lombok.Getter;
import lombok.Value;

@Getter
@Value
public class UpdateDateRequestWithString {
    private String dateAsString;
}
