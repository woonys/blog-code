package com.example.loanexample.interest;

import com.example.loanexample.loan.LoanInterestRule;

public class CashLoanInterest implements LoanInterestRule {
    private int cashLoanInterest = 10;


    @Override
    public int calculate(int loanAmount) {
        return loanAmount * (cashLoanInterest/100);
    }
    @Override
    public boolean isVip(String grade) {
        if (grade == "VIP") {
            return true;
        }
        return false;
    }
    @Override
    public LoanInterestRule get(String category) {
        return null;
    }
}
