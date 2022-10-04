package com.example.objectdesign.evaluation;

import java.time.LocalDateTime;
import java.util.Optional;

import com.example.objectdesign.domain.DateObject;

public class DateObjectEvaluationProcessor {
    private static final int OBJECT_ALIVE_DAYS = 30;

    public static boolean isDateAlive(DateObject obj) {
        return Optional.ofNullable(obj.getUpdatedAt())
                       .map(updatedAt -> updatedAt.toLocalDateTime().plusDays(OBJECT_ALIVE_DAYS))
                       .map(isDateAlive -> LocalDateTime.now().isAfter(isDateAlive))
                       .orElse(false);
    }

    public void DateAliveProcess() {
        //...
    }
}
