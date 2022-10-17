package com.example.loanexample.interest;

import com.example.loanexample.loan.LoanInterestRule;

public class WelcomeLoanInterest implements LoanInterestRule {
    private int welcomeLoanInterest = 30;

    @Override
    public int calculate(int loanAmount) {

        return loanAmount * (welcomeLoanInterest/100);
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
