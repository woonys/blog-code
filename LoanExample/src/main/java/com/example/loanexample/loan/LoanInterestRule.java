package com.example.loanexample.loan;

public interface LoanInterestRule {
    int calculate(int loanAmount);

    boolean isVip(String grade);
    LoanInterestRule get(String category);
}
