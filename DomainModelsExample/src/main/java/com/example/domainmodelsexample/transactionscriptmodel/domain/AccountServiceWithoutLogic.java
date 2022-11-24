package cc.truecredits.truecreditlms.domain.acsreport.practice.TransactionScript.Domain;

public class AccountServiceWithoutLogic {

    public String sendMoney(int requestMoney, int AccountRequest, int AccountResponse) {
        AccountWithLogic accountFromRequest = AccountRepository.findById(AccountRequest);
        AccountWithLogic accountToResponse = AccountRepository.findById(AccountResponse);

        accountFromRequest.sendMoney(requestMoney);
        accountToResponse.sendMoney(requestMoney);
    }
}
