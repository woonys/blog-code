import static org.junit.jupiter.api.Assertions.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;

class DomainTest {

    private static final String ZONE_ID_INDIA = "Asia/Kolkata";
    private static final ZoneId ZONE_ID_IST = ZoneId.of(ZONE_ID_INDIA);

    @Test
    public void Timestamp_LocalDateTime_ZonedDateTime_fail() {
        String exampleTimeBefore5MinsFromNow = "2023-01-20 08:30:30.0";
        Timestamp updatedAt = Timestamp.valueOf(exampleTimeBefore5MinsFromNow);
        ZonedDateTime indiaTimeBefore5MinsFromNow = updatedAt.toLocalDateTime().atZone(ZONE_ID_IST);
        System.out.println("indiaTimeBefore5MinsFromNow = " + indiaTimeBefore5MinsFromNow);

        ZonedDateTime indiaNow = ZonedDateTime.of(2023, 1, 20, 12, 05, 30, 0, ZONE_ID_IST);
        System.out.println("indiaNow = " + indiaNow);

        assertFalse(indiaNow.isBefore(indiaTimeBefore5MinsFromNow.plusMinutes(15)));
    }

    @Test
    public void Timestamp_Instant_ZonedDateTime_success() {
        String exampleTimeBefore5MinsFromNow = "2023-01-20 12:00:30.0"; //Timestamp는 (UTC+0) 1970년 1월 1일 00:00시를 기준으로 하기에 exampleTime은 UTC+0 기준임.
        Timestamp updatedAt = Timestamp.valueOf(exampleTimeBefore5MinsFromNow);
        ZonedDateTime indiaTimeBefore5MinsFromNow = updatedAt.toInstant().atZone(ZONE_ID_IST);
        System.out.println("indiaTimeBefore5MinsFromNow = " + indiaTimeBefore5MinsFromNow);

        ZonedDateTime indiaNow = ZonedDateTime.of(2023, 1, 20, 12, 35, 30, 0, ZONE_ID_IST);
        System.out.println("indiaNow = " + indiaNow);

        assertTrue(indiaNow.isBefore(indiaTimeBefore5MinsFromNow.plusMinutes(15)));
    }
}