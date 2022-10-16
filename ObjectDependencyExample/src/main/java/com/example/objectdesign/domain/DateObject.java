package com.example.objectdesign.domain;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DateObject {
    @Id
    private long id;

    private Timestamp updatedAt;

    private static final int OBJECT_ALIVE_DAYS = 30;

    public static boolean isDateAlive(DateObject obj) {
        return Optional.ofNullable(obj.getUpdatedAt())
                .map(updatedAt -> updatedAt.toLocalDateTime().plusDays(OBJECT_ALIVE_DAYS))
                .map(isDateAlive -> LocalDateTime.now().isAfter(isDateAlive))
                .orElse(false);
    }
}
