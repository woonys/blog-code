# ZonedDateTime & LocalDateTime & Timestamp 완벽 뽀개기

## Introduction

문제가 됐던 코드:

```java
public boolean isWithinCoolTime(long coolTime) {
        ZonedDateTime updatedAtIndianTime = updatedAt.toLocalDateTime().atZone(ZonedDateTimeUtils.IST_ZONE_ID);
        return ZonedDateTimeUtils.getIndiaNow().isBefore(updatedAtIndianTime.plusMinutes(coolTime));
}
```

테스트 찍어보니… → updatedAtIndianTime이 Timestamp에 찍힌 그대로 남더라..!(UTC+00:00)

```java
updatedAtIndianTime: 2023-02-02T 08:06:17+05:30[Asia/Kolkata]
now time: 2023-02-02T 13:37:41.331937+05:30[Asia/Kolkata]
```

아니..왜 시차가 안 바뀌었지?!

## Why it happened? → Timestamp/LocalDateTime/ZonedDateTime에 대한 이해도 부족!

```
public boolean isWithinCoolTime(long coolTime) {
    ZonedDateTime updatedAtIndianTime = updatedAt.toInstant().atZone(ZONE_ID_IST);
    return ZonedDateTimeUtils.getIndiaNow().isBefore(updatedAtIndianTime.plusMinutes(coolTime));
}
```

ChatGPT 답변을 보자. 아주 좋다...

> You could use **`timestamp.toLocalDateTime().atZone()`**, but it wouldn't provide the correct result, because **`toLocalDateTime()`** only converts the Timestamp to a **`LocalDateTime`** object, which does not have any information about the time zone or offset. When you call **`atZone()`** on a **`LocalDateTime`** object, it will apply the default system time zone to the **`LocalDateTime`**, which may not be the desired time zone.

> By using **`timestamp.toInstant().atZone(kolkataZone)`**, you first convert the Timestamp to an **`Instant`** object, which represents a point in time in UTC, and then apply the "Asia/Kolkata" time zone offset to the **`Instant`** using **`atZone`**. This ensures that the resulting **`ZonedDateTime`** object has the correct time zone and offset information.