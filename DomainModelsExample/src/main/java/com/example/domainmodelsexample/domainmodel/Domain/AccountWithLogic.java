package cc.truecredits.truecreditlms.domain.acsreport.practice.TransactionScript.Domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AccountWithLogic {
    private Long accountId;
    private int left;

    public String sendMoney(int requestMoney) {
        if (left < requestMoney) {
            return "the deposit is not enough!";
        }
        left -= requestMoney;
        return "success";
    }
}
