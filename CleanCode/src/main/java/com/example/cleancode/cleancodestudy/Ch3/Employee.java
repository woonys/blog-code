package com.example.cleancode.cleancodestudy.Ch3;

public abstract class Employee {
    public abstract boolean isPayDay();

    public abstract Money calculatePay();

    public abstract void deliverPay(Money pay);

}
