# 순환 의존(Circular Dependency) 예제

## Introduction
이전에도 짰던 코드에 순환 참조 이슈가 있었는데, 이번에도 똑같은 실수를 저질렀다..후.. 반성하는 의미에서 순환 참조 예제를 직접 짜보고 이를 어떻게 해결할 수 있을지 고민해보자. 미리 말해두는데, 의존성 주입이 이 문제를 해결하는 핵심이 아니다. DI는 하나의 방법일 뿐이다. 
그보다 해결하는 과정과 그 과정에서 어떻게 사고하는지를 중점적으로 살펴보면 훨씬 도움이 될 것이다. 우리는 핀테크 회사이니, 대출 도메인에서 일어날 수 있는 상호 참조 예제를 하나 짜보고 이를 풀어보자.

## 1. Requirements

사용자가 대출을 할 경우, 사용자가 갚아야 하는 돈인 변제액을 계산해야 한다. 변제액은 (대출 원금 + 이자)로 계산하는데, 이때, 사용자가 어떤 형태의 대출을 받았냐에 따라 그 이자율이 달라진다. 

자사에서 제공하는 대출의 유형은 크게 세 가지로, 

1. Cash Loan
2. LevelUp Loan
3. Welcome Loan

이렇게 세 가지가 있다.

사용자가 특정 유형의 대출을 받았을 때, 변제액을 계산하는 로직을 설계해보자. 이때, 조건은 아래와 같다.

1. 도메인은 대출(Loan) 도메인과 이자(Interest) 도메인으로 나눈다. 
2. 대출 도메인에서는 사용자의 대출액을 관리하며, 이자 도메인에서 계산한 최종 변제액을 반환하는 비즈니스 로직을 갖는다. 
3. 이자 도메인에서는 대출 도메인에서 관리하는 대출액을 참조해 최종 변제액을 계산하는 로직을 갖는다.

여기까지 조건을 만들고 나면 해당 요구사항에 맞게 패키지를 Loan과 Interest로 나눈 뒤, 각각에 들어갈 객체를 생성해보도록 하자.

### Loan

- Loan: 사용자가 대출한 금액에 대해 이자를 포함한 최종 변제액을 계산하는 객체.

```java
public class Loan {
    private Long id;
    private int loanAmount;
    private boolean isVip;

    public Loan(Long id, int loanAmount) {
        this.id = id;
        this.loanAmount = loanAmount;
        this.isVip = isVip;
    }

    public Long getId() {
        return id;
    }

    public int getLoanAmount() {
        return loanAmount;
    }

    public boolean isVip() {
        return isVip;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setLoanAmount(int loanAmount) {
        this.loanAmount = loanAmount;
    }

    public void setVip(boolean vip) {
        isVip = vip;
    }

    public int calculateInterest(InterestRule interestRule) {
        return interestRule.calculate(this.loanAmount);
    }
}
```

### Interest

- Interest: Interest 인터페이스는 의존성 주입의 대상으로, 사용자가 이용한 대출의 종류에 따른 Interest 클래스를 주입해준다.
```java
public interface Interest {
    int getInterestAmount(int loanAmount);

    boolean isDiscount(Loan loan);
}
```

- CashLoanInterest: CashLoan은 Loan의 대출 종류 중 하나로, 그에 따른 이자를 계산하는 객체이다.
```java
public class CashLoanInterest implements Interest{
private int cashLoanInterest = 10;

    @Override
    public boolean isDiscount(Loan loan) {
        if (loan.isVip()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int getInterestAmount(int loanAmount) {
        return loanAmount * (cashLoanInterest/100);
    }
}
```

- CashLoanInterest: LevelUpLoan 역시 CashLoan과 같은 맥락.
```java
public class LevelUpLoanInterest implements Interest{
    private int levelUpLoanInterest = 20;

    @Override
    public boolean isDiscount(Loan loan) {
        if (loan.isVip()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int getInterestAmount(int loanAmount) {
        return loanAmount * (levelUpLoanInterest/100);
    }
}
```


