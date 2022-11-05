package com.example.cleancode.cleancodestudy.Ch3;

public interface EmployeeFactory {
    public Employee makeEmployee(EmployeeRecord r) throws InvalidEmployeeType;
}
