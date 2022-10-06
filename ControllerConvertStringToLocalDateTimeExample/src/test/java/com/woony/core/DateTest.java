package com.woony.core;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.woony.core.controller.UpdateDateController;
import com.woony.core.domain.date.DateEntity;
import com.woony.core.domain.date.DateEntityRepository;
import com.woony.core.domain.exception.DateResourceNotFoundException;
import com.woony.core.domain.request.UpdateDateRequestWithString;
import com.woony.core.service.UpdateDateService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class DateTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UpdateDateService updateDateService;

    @Autowired
    private DateEntityRepository dateEntityRepository;

    @Test
    public void ControllerTestWithString() {
        String DATETIME_PATTERN_BS_FETCH_DATE = "dd-MMM-yyyy hh:mm:ss a";

        DateTimeFormatter df = new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .appendPattern(DATETIME_PATTERN_BS_FETCH_DATE)
            .toFormatter(Locale.ENGLISH);

        String beforeFetchDate = "08-Aug-2022 12:58:47 PM";

        DateEntity dateEntity = dateEntityRepository.save(DateEntity.builder()
                                            .id(1L)
                                            .title("Date")
                                            .dateTime(LocalDateTime.from(df.parse(beforeFetchDate)))
                                            .build()); // 엔티티 저장

        String afterFetchDate = "08-Apr-2032 12:58:47 PM";
        UpdateDateRequestWithString udw = new UpdateDateRequestWithString(afterFetchDate);
        Long updateId = dateEntity.getId();
        String url = "http://localhost:" + port + "/updateDateWithString/" + updateId;
        HttpEntity<UpdateDateRequestWithString> requestEntity = new HttpEntity<>(udw);

        //when
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, String.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        DateEntity savedEntity = dateEntityRepository.findById(updateId).orElseThrow(() -> new DateResourceNotFoundException("There's not entity."));
        assertThat(savedEntity.getDateTime()).isEqualTo(LocalDateTime.from(df.parse(afterFetchDate)));
    }
}
