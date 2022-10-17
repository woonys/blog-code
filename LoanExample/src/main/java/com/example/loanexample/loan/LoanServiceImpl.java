package com.example.loanexample.loan;

import java.util.Map;

public class LoanServiceImpl implements LoanService {

    private LoanRepository loanRepository;
    private Map<String, LoanInterestRule> interestRuleMap;

    public LoanServiceImpl(LoanRepositoryImpl loanRepository) {
        this.loanRepository = loanRepository;
        this.interestRuleMap = interestRuleMap;
    }

    @Override
    public Loan getLoanByLoanId(Long loanId) {
        Loan loan = loanRepository.findById(loanId);
        return loan;
    }

    @Override
    public int getLoanAmount(Long loanId) {
        Loan loan = loanRepository.findById(loanId);
        int loanAmount = loan.getLoanAmount();
        return loanAmount;
    }

//    @Override
//    public int getInterestAmount(Loan loan, Interest interest) {
//        return interest.calculate(loan.getLoanAmount());
//    }

    @Override
    public int getNealInterestAmount(Loan loan) {
        LoanInterestRule loanInterestRule = interestRuleMap.get("CL");
        return loan.calculateInterest(loanInterestRule);
    }
}
