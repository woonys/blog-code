package com.example.loanexample.interest;

import com.example.loanexample.loan.LoanInterestRule;

public class LevelUpLoanInterest implements LoanInterestRule {
    private int levelUpLoanInterest = 20;


    @Override
    public int calculate(int loanAmount) {
        return loanAmount * (levelUpLoanInterest/100);
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
