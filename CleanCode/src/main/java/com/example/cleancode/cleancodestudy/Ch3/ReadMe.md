# 3장 - 함수

프로그램의 가장 기본적인 단위: 함수.

```java
/**
* setUp 페이지와 tearDown 페이지를 테스트 페이지에 넣은 후 해당 페이지를 HTML로 렌더링하는 메소드
* @param pageData
* @param isSuite
* @return
* @throws Exception
*/
public static String renderPageWithSetupsAndTeardowns(PageData pageData, boolean isSuite) throws Exception {
    boolean isTestPage = pageData.hasAttribute("Test");
    if (isTestPage) {
        WikiPage testPage = pageData.getWikiPage();
        StringBuffer newPageContent = new StringBuffer();
        includeSetupPages(testPage, newPaageContent, isSuite);
        newPageContent.append(pageData.getContent());
        includeTearDownPages(testPage, newPageContent, isSuite);
        pageData.setContent(newPageContent.toString());
    }
    return pageData.getHtml();
}
```

위 함수는 읽기 쉬운 함수에 속한다. 이렇게 함수가 읽기 쉽고 이해하기 쉬우려면 어떻게 설계해야 할까?

## 1. 작게 만들어라

- 함수를 만드는 첫째 규칙: `작게!`
- 얼마나 작게? 길어야 4줄을 넘지 않는 게 좋다. 그냥 짧을 수록 좋다.
- if/else문 혹은 while문 등에 들어가는 블록은 한 줄인 게 좋다. 즉, 중첩 구조가 생길만큼 함수가 커져서는 안된다. 함수에서 들여쓰기 수준은 1단 정도가 적당하다.

사실 위의 함수 예제는 아래와 같이 쪼개고 더 줄일 수 있어야 한다. (책 내용에 나오는 메소드 뿐만 아니라 따로 쪼갠 메소드까지 추가했음)

```java
    public static String renderPageWithSetupsAndTearDown_refactoring(PageData pageData, boolean isSuite) throws Exception {
    if (isTestPage(pageData)) {
    includeSetupAndTearDownPages(pageData, isSuite);
    }
    return pageData.getHtml();
    }

private static boolean isTestPage(pageData pageData) {
    return pageData.hasAttribute("Test");
    }

private static void includeSetupAndTearDownPages(pageData, isSuite) {
    WikiPage testPage = pageData.getWikiPage();
    StringBuffer newPageContent = new StringBuffer();
    includeSetupPages(testPage, newPaageContent, isSuite);
    newPageContent.append(pageData.getContent());
    includeTearDownPages(testPage, newPageContent, isSuite);
    pageData.setContent(newPageContent.toString());
    }
```
## 2. 한 가지만 해라!

개선하기 전 맨 처음의 메소드는 하나의 함수 안에서 여러 가지 작업을 처리한다.
- 버퍼를 생성하고
- 페이지를 가져오고
- 상속된 페이지를 검색하고
- 경로를 렌더링하고
- 무슨 문자열을 덧붙이고
- HTML을 생성한다.

반면 개선 후 쪼갠 메소드는 한 가지만 처리한다. 설정 페이지와 해제 페이지를 테스트 페이지에 넣는다. 그리고 디테일한 작업에 대한 메소드 역시 책임이 분리되어 있다.

> 함수는 한 가지를 잘해야 한다. 
> 그 한 가지를 잘해야 한다. 
> 그 한 가지만을 잘해야 한다.

여기서 `renderPageWithSetupsAndTearDown()`은 한 가지만 한다고 할 수 있나? 사실 제대로 보면 이 함수는 세 가지를 한다고 말할 수도 있다.

1. 페이지가 테스트 페이지인지 판단한다.
2. 그렇다면 설정 페이지와 해제 페이지를 넣는다.
3. 페이지를 HTML로 렌더링한다.

하지만 가짓수를 결정하는 건 행위의 수가 아닌 메소드로 드러나는 추상화의 단계로 결정한다. 예컨대 위 메소드 이름이 renderPageWithSetupsAndTearDown이기에
이 메소드 이름에서 추상화 수준이 하나인 단계만 수행한다면 이 함수는 한 가지 작업만 한다고 볼 수 있다. 함수를 만드는 이유는 큰 개념(=함수 이름)을 다음 추상화 수준에서 여러 단계로 나눠 수행하기 위해서이기 때문.

여기서 함수가 한 가지만 하는지 판단하는 방법이 하나 더 있다. 단순히 다른 표현이 아니라 의미 있는 이름으로 다른 함수를 추출할 수 있다면 그 함수는 여러 작업을 한다고 봐야 한다.

## 3. 함수당 추상화 수준은 하나로!

위에서 말한 내용과 동일. 함수가 확실히 한 가지 작업만 하려면 함수 내 모든 문장의 추상화 수준이 동일해야 한다. 다시 개선 전 메소드를 보자.
- getHtml()은 추상화 수준이 아주 높다.
- String pagePathName = PathParser.render(pagePath)는 추상화 수준이 중간이다.
- .append()는 추상화 수준이 아주 낮다.

한 함수 내에 추상화 수준을 섞으면 코드를 읽는 사람이 헷갈린다. 특정 표현이 근본 개념인지 아니면 세부사항인지 구분하기 어렵기 때문이다.

### 위에서 아래로 코드 읽기: 내려가기 규칙

코드는 위에서 아래로 이야기처럼 읽혀야 좋다. 한 함수 다음에는 추상화 수준이 한 단계 낮은 함수가 온다. 즉, 위에서 아래로 프로그램을 읽으면 함수 추상화 수준이
한 번에 한 단계씩 낮아진다. 이를 내려가기 규칙이라 부른다.

핵심은 짧으면서도 한 가지만 하는 함수다. 위에서 아래로 TO 문단을 읽어내려 가듯이 코드를 구현하면 추상화 수준을 일관되게 유지하기가 쉬워진다.

## Switch 문의 경우는?

switch 문은 작게 만들기 어렵다. 한 가지 작업만 하는 switch문도 만들기 어렵다. 본질적으로 switch문은 N가지 작업을 처리하기 때문이다. 불행히도 switch문을 완전히 
피할 방법은 없다. 하지만 각 switch문을 저차원 클래스에 숨기고 절대 반복하지 않는 방법은 있다. 이는 다형성을 이용한 방법이다.

아래 함수를 살펴보자. 직원 유형에 따라 다른 값을 계산해 반환하는 함수다.
```java
public Money calculatePay(Employee e) {
        switch (e.type) {
            case COMMISSIONED:
                return calculateCommissionedPay(e);
            case HOURLY:
                return calculateHourlyPay(e);
            case SALARIED:
                return calculatedSalariedPay(e);
            default:
                throw new InvalidEmployeeType(e.type);
        }
    }
```
위 함수에는 몇 가지 문제가 있다.
1. 함수가 길다. 새 직원 유형을 추가하면 더 길어진다.
2. 한 가지 작업만을 수행하지 않는다.
3. SRP(단일 책임 원칙 - 하나의 객체는 반드시 하나의 동작에 대한 책임만을 갖는다)를 위반한다. 코드를 변경할 이유가 여럿이기 때문이다.
4. OCP(개방 폐쇄 원칙 - 객체의 확장에는 열려있고 변경(수정)에는 닫혀있어야 한다)를 위반한다. 새 직원 유형을 추가할 때마다 코드를 변경해야 하기 때문이다.
5. 위 함수와 구조가 동일한 함수가 무한정 존재한다. 예컨대 다음과 같은 함수가 가능하다.
   - isPayDay(Employee e, Date date);
   - deliverDay(Employee e, Money pay);
   이러한 가능성은 무한하다. 그리고 모두 다 유해한 구조이다.
   
이 문제를 해결한 코드를 보자. 이 방식은 switch문을 추상 팩토리에 꽁꽁 숨긴 구조이다.
- 팩토리는 switch문을 사용해 적절한 Employee 파생 클래스의 인스턴스를 생성한다.
- calculatePay, isPayDay, deliverPay 등과 같은 함수는 Employee 인터페이스를 거쳐 호출된다. 
- 그러면 다형성으로 인해 실제 파생 클래스의 함수가 실행된다.

```java
// Employee

public abstract class Employee {
   public abstract boolean isPayDay();

   public abstract Money calculatePay();

   public abstract void deliverPay(Money pay);

}

// EmployeeFactory

public interface EmployeeFactory {
   public Employee makeEmployee(EmployeeRecord r) throws InvalidEmployeeType;
}


// EmployeeFactoryImpl

public class EmployeeFactoryImpl implements EmployeeFactory {
   public Employee makeEmployee(EmployeeRecord r) throws InvalidEmployeeType {
      switch (r.type) {
         case COMMISSIONED:
            return calculateCommissionedPay(r);
         case HOURLY:
            return calculateHourlyPay(r);
         case SALARIED:
            return calculatedSalariedPay(r);
         default:
            throw new InvalidEmployeeType(r.type);
      }
   }
}
```
일반적으로 switch문을 한 번은 참아준다. 다형적 객체를 생성하는 코드 안에서는. 이렇게 상속 관계로 숨긴 후에는 절대로 다른 코드에 노출하지 않는다.