package com.example.domainmodelexample.domainmodel.service;

import com.example.domainmodelexample.domainmodel.Domain.*;
import com.example.domainmodelexample.repository.AccountRepository;

public class AccountServiceWithoutLogic {

    public String sendMoney(int requestMoney, int AccountRequest, int AccountResponse) {
        AccountWithLogic accountFromRequest = AccountRepository.findById(AccountRequest);
        AccountWithLogic accountToResponse = AccountRepository.findById(AccountResponse);

        accountFromRequest.sendMoney(requestMoney);
        accountToResponse.sendMoney(requestMoney);
    }
}
