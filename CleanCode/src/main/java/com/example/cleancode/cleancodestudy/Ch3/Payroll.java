package com.example.cleancode.cleancodestudy.Ch3;

public class Payroll {
    public Money calculatePay(Employee e) {
        switch (e.type) {
            case COMMISSIONED:
                return calculateCommissionedPay(e);
            case HOURLY:
                return calculateHourlyPay(e);
            case SALARIED:
                return calculatedSalariedPay(e);
            default:
                throw new InvalidEmployeeType(e.type);
        }
    }
}
