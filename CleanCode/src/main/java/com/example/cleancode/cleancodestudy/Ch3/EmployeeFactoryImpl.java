package com.example.cleancode.cleancodestudy.Ch3;

public class EmployeeFactoryImpl implements EmployeeFactory {
    public Employee makeEmployee(EmployeeRecord r) throws InvalidEmployeeType {
        switch (r.type) {
            case COMMISSIONED:
                return calculateCommissionedPay(r);
            case HOURLY:
                return calculateHourlyPay(r);
            case SALARIED:
                return calculatedSalariedPay(r);
            default:
                throw new InvalidEmployeeType(r.type);
        }
    }
}
