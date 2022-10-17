package com.example.loanexample.loan;


public class Loan {
    private Long id;
    private int loanAmount;
    private boolean isVip;

    public Loan(Long id, int loanAmount) {
        this.id = id;
        this.loanAmount = loanAmount;
        this.isVip = isVip;
    }

    public Long getId() {
        return id;
    }

    public int getLoanAmount() {
        return loanAmount;
    }

    public boolean isVip() {
        return isVip;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setLoanAmount(int loanAmount) {
        this.loanAmount = loanAmount;
    }

    public void setVip(boolean vip) {
        isVip = vip;
    }

    public int calculateInterest(LoanInterestRule loanInterestRule) {
        return loanInterestRule.calculate(this.loanAmount);
    }
}
