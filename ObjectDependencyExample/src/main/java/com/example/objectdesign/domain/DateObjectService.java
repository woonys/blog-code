package com.example.objectdesign.domain;

import java.sql.Timestamp;

import com.example.objectdesign.evaluation.DateObjectEvaluationProcessor;

public class DateObjectService {
    private final DateObjectRepository dateObjectRepository;
    private final InjectionA injectionA;
    private final InjectionB injectionB;
    private final InjectionC injectionC;

    public DateObjectService(DateObjectRepository dateObjectRepository, InjectionA injectionA, InjectionB injectionB, InjectionC injectionC) {
        this.dateObjectRepository = dateObjectRepository;
        this.injectionA = injectionA;
        this.injectionB = injectionB;
        this.injectionC = injectionC;
    }

    public DateObject getDateObject(DateObject object) {
        return object;
    }

    public void updateObjectDateTime(DateObject dobj, Timestamp updateTime) {
        if (DateObjectEvaluationProcessor.isDateAlive(dobj)) {
            dobj.setUpdatedAt(updateTime);
            dateObjectRepository.save(dobj);
        } else {
            System.out.println("This object is expired.");
        }
    }

}
