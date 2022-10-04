package com.example.objectdesign;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.objectdesign.domain.DateObject;
import com.example.objectdesign.domain.DateObjectService;
import com.example.objectdesign.evaluation.DateObjectEvaluationProcessor;

@ExtendWith(MockitoExtension.class)
public class DateObjectEvaluationProcessorTest {
    @Mock
    private DateObject dateObject;
    @Mock
    private DateObjectService dateObjectService;

    @InjectMocks
    private DateObjectEvaluationProcessor doep;

    @Test
    void test() {
        when(dateObjectService.getDateObject(any())).thenReturn(new DateObject(1L, Timestamp.valueOf(LocalDateTime.now())));

    }
}
