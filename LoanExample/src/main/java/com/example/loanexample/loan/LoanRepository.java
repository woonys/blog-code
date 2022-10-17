package com.example.loanexample.loan;

public interface LoanRepository {
    void save(Loan loan);

    Loan findById(Long loanId);
}
