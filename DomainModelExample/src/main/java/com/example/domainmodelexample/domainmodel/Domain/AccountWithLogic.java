package com.example.domainmodelexample.domainmodel.Domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AccountWithLogic {
    private int accountId;
    private int left;

    public AccountWithLogic(int accountRequest, int i) {
        this.accountId = accountRequest;
        this.left = i;
    }

    public String sendMoney(int requestMoney) {
        if (left < requestMoney) {
            return "the deposit is not enough!";
        }
        left -= requestMoney;
        return "success";
    }
}
