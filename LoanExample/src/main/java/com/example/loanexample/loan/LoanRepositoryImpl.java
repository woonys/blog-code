package com.example.loanexample.loan;

import java.util.HashMap;
import java.util.Map;

public class LoanRepositoryImpl implements LoanRepository{
    private static Map<Long, Loan> Bank = new HashMap<>();

    @Override
    public void save(Loan loan) {
        Bank.put(loan.getId(), loan);
    }

    @Override
    public Loan findById(Long loanId) {
        return Bank.get(loanId);
    }
}
