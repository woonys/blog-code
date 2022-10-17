package com.example.loanexample.loan;

public interface LoanService {

    Loan getLoanByLoanId(Long loanId);
    int getLoanAmount(Long loanId);

//    int getInterestAmount(Loan loan, Interest interest);

    int getNealInterestAmount(Loan loan);
}
