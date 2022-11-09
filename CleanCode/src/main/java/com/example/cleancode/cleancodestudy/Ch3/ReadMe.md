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

## 4. 서술적인 이름을 사용하라!

앞의 예제에서 함수 이름을 `testableHtml`에서 `SetupTeardownIncluder.render`로 바꿨다. 이는 함수가 하는 일을 좀 더 잘 표현하므로 훨씬 좋은 이름이다.
역시 private 함수에서 `isTestable`, `includeSetupAndTearDownPages` 등과 같이 서술적인 이름을 지었다.
서술적인 이름을 지을 때 지켜야 할 원칙이 있다.

- 이름을 읽으면서 짐작했던 기능을 코드가 수행한다면 깨끗한 함수 이름이다.
- 함수가 작고 단순할수록 서술적인 이름을 고르기도 쉬워진다.
- 이름이 길어도 괜찮다. 길고 서술적인 이름이 짧고 어려운 이름보다 좋다.
- 이름을 붙일 때는 일관성이 있어야 한다. 모듈 내에서 함수 이름은 같은 문구, 명사, 동사를 상ㅇ한다.

### 함수 인수
함수에서 이상적인 인수 개수는 0개이다. 다음은 1개이고, 그 다음은 2개이다. 3개 이상은 가능한 피하는 게 좋다.

왜? 인수는 어렵기 때문이다. 인수는 개념을 이해하기 어렵게 만든다.

테스트 관점에서 인수는 더 어렵다. 갖가지 인수 조합으로 함수를 검증하는 테스트 케이스를 작성한다고 상상해보자. 인수가 없다면 훨씬 간단해진다.

최선은 입력 인수가 없는 경우이며, 차선은 1개이다.

### 많이 쓰는 단항 형식

인수는 0개가 좋다고 했으나 인수로 1개를 넘기는 이유가 일반적으로 2가지(+드물게 1가지 추가)가 있다. 

1. 인수에 질문을 던지는 경우

`boolean fileExists("MyFile")` -> MyFile이라는 이름을 갖는 파일이 존재하는지 질문을 던지는 함수

2. 인수로 뭔가를 변환해 결과를 반환하는 경우

`InputStream fileOpen("MyFile")` -> String 형의 파일 이름을 InputStream으로 변환하는 함수

이외에 드물게 사용하나 유용한 단항 함수 형식이 `이벤트`이다. 이벤트는 입력 인수만 있고 출력 인수는 없다. 프로그램은 함수 호출을
이벤트로 해석해 입력 인수로 시스템 상태를 바꾼다.

이벤트 함수는 조심해서 사용해야 한다. 이벤트라는 사실이 코드에 명확히 드러나야 한다.

지금까지 설명한 경우가 아니면 단항 함수는 가급적 피한다. 예를 들어 `void includeSetupPageInto(StringBuffer pageText)`는 피한다.
변환 함수에서 출력 인수를 사용하면 혼란을 일으킨다. 입력 인수를 변환하는 함수라면 변환 결과는 반환값으로 돌려준다.

### 플래그 인수

플래그 인수는 추하다. 함수로 bool 값을 넘기는 관례는 끔찍하다. 플래그를 쓴다는 자체가 함수가 한꺼번에 여러 가지를 처리한다는 뜻이니까.
`플래그가 참이면 이걸 하고 거짓이면 저걸 해라`라는 의미지 않나.

### 이항 함수

인수가 2개인 함수는 1개인 함수보다 이해하기 어렵다. 예를 들어 
`writeField(name)`은 `writeField(outputStream, name)`보다 이해하기 쉽다.

물론 이항 함수가 적절한 경우도 있다.

```java
Point p = new Point(0, 0)
```
위의 좌표 예시는 적절한 예이다. 직교 좌표계 점은 일반적으로 인수 2개를 취한다. 하지만 여기에 쓰이는 2개의 인수는 한 값을 표현하는 두 요소다.
두 요소에는 자연적인 순서도 있따. 하지만 위의 writeField 예시에서 outputStream과 name은 한 값을 표현하지도, 자연적인 순서가 있지도 않다.

아주 당연하게 쓰이는 assertEquals(expected, actual)에도 문제가 있다. expected 인수에 actual 값을 집어넣는 실수가 얼마나 많나(나도 똑같이 실수한 경험이 있음...)
두 인수는 자연적인 순서가 없다. expected 다음에 actual이 온다는 순서를 인위적으로 기억해야 한다.

이항 함수가 무조건 나쁘다는 소리는 아니다. 하지만 그만큼 위험하니 가능하면 단항 함수로 바꾸도록 애써야 한다.

## 5. 부수 효과를 일으키지 마라!

함수에서 한 가지를 하겠다고 약속하고선 남몰래 다른 짓을 하는 부수 효과는 일으켜서는 안된다. 여기서 부수 효과란, 
예상치 못하게 클래스 변수/함수로 넘어온 인수/시스템 전역 변수 등 예상치 못하게 변수 값을 수정하는 짓을 말한다. 예제를 보자.

```java
public class UserValidator {
    private Cryptographer cryptographer;

    public boolean checkPassword(String userName, String password) {
        User user = UserGateway.findByName(userName);
        if (user != User.NULL) {
            String codePhrase = user.getPharaseEncodedByPassword();
            String phrase = cryptographer.decrypt(codedPhrase, password);
            if ("Valid Password".equals(phrase)) {
                Session.initialize();
                return true;
            }
        }
        return false;
    }
}
```

여기서 함수 checkPassword()가 일으키는 부수 효과는 Session.initialize()를 호출할 때다. checkPassword는 이름 그대로 암호를 확인하는 함수이다.
이름만 봐서는 세션을 초기화한다는 사실이 드러나지 않는다. 그래서 함수 이름만 보고 함수를 호출하는 사용자는 
사용자를 인증하면서 initialize()까지 같이 호출해 기존 세션 정보를 지워버리는 일을 자기도 모르는 새 감행하게 된다.

위의 checkPassword 함수는 특정 상황, 그러니까 세션을 초기화해도 괜찮은 경우에만 호출 가능한 함수다. 이러한 부수 효과를 일으키지 않으려면 
이런 효과까지 함수명에 명시해주는 게 좋다. 이를테면 위 함수명은 `checkPasswordANdInitializeSession`이라고 짓는 게 훨씬 좋다. 물론 함수가 한 가지 일만 한다는 규칙은 위반하긴 하지만.

## 6. 명령과 조회를 분리하라!

함수는 아래 둘 중 하나만 한다고 보면 된다. 둘 다 하면 안 된다.

1. 뭔가를 수행하거나 - 객체 상태를 변경
2. 뭔가에 답하거나 - 객체 정보를 반환

만약 하나의 함수가 위의 두 가지를 다 하면 혼란을 일으킬 수 있다. 예시를 보자.

`public boolean set(String attribute, String value);`

이 함수는 이름이 attribute인 속성을 찾아 값을 value로 설정한 후(1. 상태 변경) 성공하면 true/실패하면 false를 반환(2. 객체 정보를 반환)하는 함수이다.
이때, 이 함수를 사용하는 괴상한 예시가 등장한다.

`if (set("username", "unclebob"))...`

이 함수가 무엇을 뜻할지 시그니쳐만 보고 생각해보자. "username"이 "unclebob"으로 설정되어 있는지 확인하는 코드인가?
아니면 "username"을 "unclebob"으로 설정하는 코드인가? 이렇듯 함수를 호출하는 코드만 봐서는 의미가 모호하다. 
이는 set이라는 단어가 동사인지 형용사인지 구분하기 어렵기 때문이다. 구현 당시에는 `set`을 동사로 의도했으나 if문에 넣고 보면 형용사로 느껴진다.
함수 이름을 바꿀 수도 있지만 애초에 객체 자체를 명령하는 위치에 넣었다면 이런 일은 없었을 것이다.

진짜 해결책은 명령과 조회를 분리해 애초에 혼란을 없애는 것이다.

## 7. 오류 코드보다 예외를 사용하라!

명령 함수에서 오류 코드를 반환하는 방식은 명령/조회 분리 규칙을 미묘하게 위반한다.

`if (deletePage(page) == E_OK)`

위 코드는 동사/형용사 혼란을 일으키지는 않으나 여러 단계로 중첩되는 코드를 만들어낸다. 

```java
if (deletePage(page) == E_OK) {
            if (registry.deleteReference(page.name) == E_OK) {
                if (configKeys.deleteKey(page.name.makeKey()) == E_OK) {
                    logger.log("page deleted");
                } else {
                    logger.log("configKey not deleted");
                }
            } else {
                logger.log("deleteReference from registry failed");
            }
        } else {
            logger.log("delete failed");
            return E_ERROR;
        }
```

반면에 오류 코드 대신 예외를 사용하면 오류 처리 코드가 원래 코드에서 분리되므로 코드가 깔끔해진다.

```java
try {
    deletePage(page);
    registry.deleteReference(page.name);
    configKeys.deleteKey(page.name.makeKey());
}
catch (Exception e) {
    logger.log(e.getMessage());
}
```

### Try/Catch 블록 뽑아내기

try/catch 블록은 구리다. 코드 구조에 혼란을 일으키며, 정상 동작과 오류 처리 동작을 뒤섞는다. 그러니 try/catch 블록을 별도 함수로 뽑아내는 편이 좋다.

```java
public void delete(Page page) {
    try {
        deletePageAndAllReferences(page);
     } catch (Exception e) {
        logError(e);
     }
}


private void deletePageAndAllReferences(Page page) throws Exception {
        deletePage(page);
        registry.deleteReference(page.name);
        configKeys.deleteKey(page.name.makeKey());
}

private void logError(Exception e) {
        logger.log(e.getMessage());
}
```

위에서 delete 함수는 모든 오류를 처리한다. 그러니 코드를 이해하기 훨씬 쉽다. 그런데 실제로 페이지를 제거하는 함수는 deletePageAndAllReferences다.
deletePageAndAllReferences 함수는 예외를 처리하지 않는다. 이렇게 정상 동작과 오류 처리 동작을 분리하면 코드를 이해하고 수정하기 훨씬 쉽다.

### 오류 처리도 한 가지 작업이다.

함수는 `한 가지 작업만`해야 한다. 다시. 함수는 한 가지 작업만 해야 한다. 오류 처리 역시 한 가지 작업에 속한다.
따라서 오류를 처리하는 함수는 오류만 처리해야 한다. 함수에 키워드 try가 있다면
함수는 try 문으로 시작해 catch/finally로 끝나야 한다는 말이다.

### Error.java 의존성 자석

오류 코드를 반환한다는 말은 곧 클래스든 열거형 변수든, 어디선가 오류 코드를 정의한다는 뜻이다.

```java
public enum Error {
    OK,
   INVALID,
   NO_SUCH,
   LOCKED,
   OUT_OF_RESOURCES,
   WAITING_FOR_EVENT;
}
```

위와 같은 클래스는 의존성 자석이다. 다른 클래스에서 이 에러 enum을 import해 사용해야 하기 때문이다.
Error enum이 변하면 Error enum을 사용하는 클래스 전부를 다시 컴파일하고 다시 배치해야 한다. 그래서 Error 클래스 변경이 어려워진다.

이때, 오류 코드 대신 예외를 사용하면 새 예외는 Exception 클래스에서 파생된다. 따라서 재컴파일/재배치 없이도 새 예외 클래스를 추가할 수 있다.

## 8. 반복하지 마라!

증복은 소프트웨어에서 모든 악의 근원이다. 많은 원칙 및 기법이 중복을 없애거나 제어할 목적으로 나왔다.
RDB에서 정규 형식은 자료에서 중복을 제거할 목적으로 만들어졌고, OOP에서는 부모 클래스에 중복되는 내용을 한데 모아버림으로써 중복을 없앤다.

### 구조적 프로그래밍

다익스트라의 구조적 프로그래밍 원칙에 따르면 모든 함수와 함수 내 모든 블록에 입구와 출구는 하나만 존재해야 한다.
즉, 함수는 return 문이 하나여야 한다는 뜻이다. 루프 안에서 break나 continue를 써서는 안된다. 하지만 이는 함수가 클 경우에나 이득을 보지
함수가 작다면 굳이 원칙처럼 따라야 할 정도는 아니다. 오히려 함수를 작게 만든다면 return, break, continue를 여러 차례 사용해도 괜찮을 수 있다.

### 함수를 어떻게 짜죠?

소프트웨어를 짜는 행위는 글짓기와 비슷하다. 논문이나 기사를 작성할 때는
1. 먼저 생각을 기록한 후 읽기 좋게 다듬는다. 이때 초안은 대개 서투르고 어수선하다.
2. 초안을 쓰고나면 원하는 대로 읽힐 때까지 말을 다듬고 문장을 고치고 문단을 정리한다.

함수를 짤 때도 마찬가지다.
1. 처음에는 길고 복잡하다. 들여쓰기 단계도 많고 중복 루프도 많다. 인수 목록도 아주 길다. 이름은 즉흥적이고 코드는 중복된다.
2. 이 서투른 코드에 대해 빠짐없이 테스트하는 단위 테스트 케이스 역시 같이 만든다.
3. 그 다음 코드를 다듬고, 함수를 만들고, 이름을 바꾸고, 중복을 제거한다. 메서드를 줄이고 순서를 바꾼다. 때로는 전체 클래스를 쪼개기도 한다. 그 와중에도 코드는 항상 단위 테스트를 통과하고 있어야 한다.

핵심: 처음부터 완벽한 함수를 턱 하고 짜내지 않는다. 이게 가능한 사람은 없다고 보는 게 맞다.

## 결론

함수는 동사이고 클래스는 명사이다. 프로그래밍의 기술은 언제나 언어 설계의 기술이다.

대가 프로그래머는 시스템을 프로그램이 아닌 이야기로 여긴다. 프로그래밍 언어라는 수단을 사용해
더 풍부하고 표현력이 강한 언어를 만들어 이야기를 풀어간다. 시슽메에서 발생하는 모든 동작을 설명하는 함수 계층이
바로 그 언어에 속한다.

이 장은 함수를 잘 만드는 기교를 소개했다. 여기서 설명한 규칙을 따른다면

- 길이가 짧고
- 이름이 좋고
- 체계가 잡힌
함수가 나올 것이다. 하지만 

> 진짜 목표는 시스템이라는 이야기를 풀어가는 데 있다는 사실을 명심하기 바란다.

```java
public class SetupTeardownIncluder {
    private PageData pageData;
    private boolean isSuite;
    private WikiPage wikiPage;
    private StringBuffer newPageContent;
    private PageCrawler pageCrawler;
    
    public static String render(PageData pageData) throws Exception {
        return render(pageData, false);
    }
    
    public static String render(PageData pageData, boolean isSuite) throws Exception {
        return new SetupTeardownIncluder(pageData).render(isSuite);
    }

    private SetupTeardownIncluder(PagedData pagedData) {
        this.pageData = pagedData;
        testPage = pagedData.getWikiPage();
        pageCrawler = testPage.getPageCrawler();
        newPageContent = new StringBuffer();
    }
    
    private String render(boolean isSuite) throws Exception {
        this.isSuite = isSuite;
        if (isTestPage())
            includeSetupAndTeardownPages();
            return pageData.getHtml;
    }
    
    private boolean isTestPage() throws Exception {
        return pageData.hasAttribute("Test");
    }
    
    private void includeSetupAndTeardownPages() throws Exception {
        includeSetupPages();
        includePageContent();
        includeTeardownPages();
        updatePageContent();
    }
    
    private void includeSetupPages() throws Exception {
        if (isSuite) {
            includeSuiteSetupPage();
        }
        includeSetupPages();
    }
    
    private void includeSuiteSetupPage() throws Exception {
        include(SuiteResponder.SUITE_SETUP_NAME, "-setup");
    }
    
    private void includeSetupPage() throws Exception {
        include("SetUp", "-setup");
    }
    
    private void includePageContent() throws Exception {
        newPageContent.append(pageData.getContent());
    }
    
    private void includeTeardownPages() throws Exception {
        includeTeardownPages();
        if (isSuite)
            includeSuiteTeardownPage();
    }
    
    private void includeTeardownPage() throws Exception {
        include("TearDown", "-teardown");
    }
    
    private void includeSuiteTeardownPage() throws Exception {
        include(SuiteResponder.SUITE_TEARDOWN_NAME, "-teardown");
    }
    
    private void updatePageContent() throws Exception {
        include(SuiteResponder.SUITE_TEARDOWN_NAME, "-teardown");
    }
    
    private void include(String pageName, String arg) throws Exception {
        WikiPage inheritedPage = findInheritedPage(pageName);
        if (inheritedPage != null) {
            String pagePathName = getPathNameForPage(inheritedPage);
            buildIncludeDirective(pagePathName, arg);
        }
    }
    
    private WikiPage findInheritedPage(String pageName) throws Exception {
        return pageCrawlerImpl.getInheritedPage(pageName, testPage);
    }
    
    private String getPathNameForPage(WikiPage page) throws Exception {
        WikiPagePath pagePath = pageCrawler.getFullPath(page);
        return PathParser.render(pagePath);
    }

    private void buildIncludeDirective(String pagePathName, String arg) {
        newPageContent.append("\n!include")
            .append(arg)
            .append(" .")
            .append(pagePathName)
            .append("\n");
    }
}
```