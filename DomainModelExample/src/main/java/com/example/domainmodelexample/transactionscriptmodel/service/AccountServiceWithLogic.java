package com.example.domainmodelexample.transactionscriptmodel.service;

import com.example.domainmodelexample.repository.AccountRepository;
import com.example.domainmodelexample.transactionscriptmodel.domain.AccountWithoutLogic;

public class AccountServiceWithLogic {

    public String sendMoney(int requestMoney, int requestAccount, int responseAccount) {
        AccountWithoutLogic accountWithLogicFromRequest = AccountRepository.findById(requestAccount);
        int leftRequest = accountWithLogicFromRequest.getLeft();
        if (leftRequest < requestMoney) {
            return "Your money isn't enough to send";
        }

        AccountWithoutLogic accountWithoutLogicToResponse = AccountRepository.findById(responseAccount);
        int leftResponse = accountWithoutLogicToResponse.getLeft() + requestMoney;
        accountWithoutLogicToResponse.setLeft(leftResponse);
        return "Success";
    }
}
