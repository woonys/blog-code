package cc.truecredits.truecreditlms.domain.acsreport.practice.TransactionScript.service;

public class AccountServiceWithLogic {

    public String sendMoney(int requestMoney, int requestAccount, int responseAccount) {
        AccountWithoutLogic accountWithLogicFromRequest = AccountRepository.findbyId(requestAccount);
        int leftRequest = accountWithLogicFromRequest.getLeft();
        if (leftRequest < requestMoney) {
            return "Your money isn't enough to send";
        }

        AccountWithoutLogic accountWithoutLogicToResponse = AccountRepository.findbyId(responseAccount);
        int leftResponse = accountWithoutLogicToResponse.getLeft() + requestMoney;
        accountWithoutLogicToResponse.setLeft(leftResponse);
        return "Success";
    }
}
