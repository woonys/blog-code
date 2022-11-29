package com.example.domainmodelexample.repository;

import com.example.domainmodelexample.domainmodel.Domain.AccountWithLogic;
import com.example.domainmodelexample.transactionscriptmodel.domain.AccountWithoutLogic;

public class AccountRepository {

    public static AccountWithLogic findById(int accountRequest) {
        return new AccountWithLogic(accountRequest, 0);
    }

    public static AccountWithoutLogic findById(int accountRequest) {
        return new AccountWithoutLogic();
    }
}
