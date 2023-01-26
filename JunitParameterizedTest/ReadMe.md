# @ParameterizedTest: 반복적인 테스트를 한 번에 돌릴 수는 없을까?🤔

## Introduction

버그 수정 티켓을 발급받았다. 이메일 검증 로직 관련 버그였는데, 마이크로서비스 간에 서로 다른 이메일 validator를 써서 일어난 문제였다.

문제의 원인을 파악한 뒤, 동일한 validator를 쓰도록 변경하고 나서 테스트 코드를 작성했다. 그런데 넣어줘야 할 이메일 예시만 다를 뿐, 나머지는 전부 동일한 로직의 코드를 매번 작성하려니 여간 찜찜할 수가 없었다. 반복적인 테스트를 한 번에 돌릴 방법이 없을지 고민했다. 마침 코드 리뷰에서 `@ParameterizedTest`를 쓰는 게 어떻겠냐는 리뷰를 받았다.

이번 시간에는 `@ParameterizedTest`의 정체와 이를 사용해 어떻게 반복적인 테스트를 자동화할 수 있을지 예제를 살펴보도록 하자.

(모든 코드는 github에 올려뒀다.)

## 환경설정

이번 예제 프로젝트는 아래와 같은 설정으로 작업했다. 스프링부트는 따로 쓰지 않고 순수 자바 코드 및 테스트 관련 의존성을 추가해 작업했다.

- Java 11(Corretto-11)
- Gradle
- Junit5
  - Junit-jupiter-api:5.8.1
  - Junit-jupiter-engine:5.8.1
  - Junit-jupiter-params:5.8.1 (이번 예제에서 사용할 @ParameterizedTest의 의존성을 포함하는 패키지다.)
- Lombok



이때 잠깐 짚고 넘어가야 할 게 있다. 본 예제에서는 스프링부트를 따로 쓰지 않았기에 테스트 코드 역시 순수 자바 코드로 동작한다.
우리는 롬복을 사용하였기에 annotationProcessor와 gradle의 testImplementation에 역시 롬복을 반드시 추가해줘야 한다. 그래야 테스트 코드를 동작할 때 엔티티 내 getter, setter 등을 호출할 텐데 이게 제대로 동작한다.

```java
//build.gradle

dependencies {
        ...
        annotationProcessor 'org.projectlombok:lombok:1.18.22'
        testImplementation'org.projectlombok:lombok:1.18.22' // 테스트에도 반드시 롬복 추가해줘야 함! 요거 안돼서 테스트 안 돌아갔네..
        ...
}
```


여기서 각각 의존성에 롬복을 추가하지 않으면 아래와 같이 `error: cannot find symbol` 에러를 맞닥뜨리게 된다.

![img.png](img.png)

위의 에러를 맞닥뜨리지 않게 조심하고, 곧바로 본론으로 넘어가보도록 하자.

## 예제: WOONY 클럽

해당 예제를 어떤 식으로 만들면 좋을지 고심하다가 뜬금없이 클럽이 떠올랐다. 우리는 지금부터 메타버스 클럽을 만들 예정이다.

이 클럽에는 아래와 같은 비즈니스 요구사항이 있다.

- 클럽
  - 클럽은 라운지와 룸으로 구성되어 있다.
  - 클럽에는 특정 조건을 만족한 사람만 입장 가능하다.
    - 클럽은 성인 이상만 입장 가능하다.
    - 만약 고객이 남자일 때, 해당 남자 등급이 브론즈 이하면 유저는 입장할 수 없다.(뺀찌)
  - 클럽 룸은 VIP 등급의 남성 혹은 골드 등급 이상의 여성만 입장 가능하다.
- 고객
  - 유저는 이름, 나이, 성별, 등급을 갖는다.
  - 유저는 클럽에 입장할 때 성인 여부를 체크받는다.
  - 유저는 클럽에 입장할 때 등급을 체크받는다.
- 등급
  - 등급은 NO_ANSWER, BRONZE, SILVER, GOLD, VIP 총 5개가 있으며, 순서가 뒤일수록 등급이 높다.

모든 코드는 깃허브에 올려뒀으니 여기서 살펴보기로 하고, 곧바로 테스트로 넘어가보자.

## ClubServiceTest: 20살 이하는 다 걸러라

20세(만 18세) 이상만 입장 가능한 클럽에 16, 17, 18세의 민짜들이 와서 들여보내달라 한다고 해보자. 테스트 코드는 아래와 같다.

    
    }
```java
public void cannotEnterClub_ifUserIsAge16() {
        //given
        int age = 16;
        user.setGender(Gender.MALE);
        user.setAge(age);
        user.setGrade(Grade.SILVER);

        //when
        boolean result = clubService.isEnterClub(user);

        //then
        assertEquals(false, result);
```

문제는 age를 제외하면 전부 동일한 코드인데 17세, 18세인 테스트 케이스에 대해 일일이 테스트 메서드를 추가해야된다는 점이다. 딱 봐도 비효율적이지 않나.

이때 우리가 쓸 수 있는 게 바로 JUnit의 `@ParameterizedTest`이다. 적용한 코드를 바로 보자.

```java
@ParameterizedTest
@ValueSource(ints = {16, 17, 18})
public void cannotEnterClub_ifUserIsNotAdult(int age) {
        //given
        user.setGender(Gender.MALE);
        user.setAge(age);
        user.setGrade(Grade.SILVER);

        //when
        boolean result = clubService.isEnterClub(user);

        //then
        assertEquals(false, result);
        }
```

위와 같이 테스트 메소드 위에 @ParameterizedTest 어노테이션을 추가한 뒤, 넣어주고자 하는 Value에 대한 Source를 @ValueSource()에 추가한다. 이때, 우리가 넣으려는 값은 int 타입이니 ints = {}에 값을 넣어주면 된다.