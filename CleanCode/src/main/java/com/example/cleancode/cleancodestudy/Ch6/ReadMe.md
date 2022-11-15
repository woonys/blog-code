# 6장 - 객체와 자료 구조

> 변수를 비공개로 정의하는 이유가 있다. 남들이 변수에 의존하지 않게 만들고 싶어서다.
> 그런데 어째서 수많은 프로그래머가 조회(get) 함수와 설정(set) 함수를 당연하게 public으로 설정해
> 비공개 변수를 외부에 노출할까?


## 자료 추상화

> 자료를 세세하게 공개하기보다는 추상적인 개념으로 표현하는 것이 좋다.

똑같이 2차원 점을 표현하는 두 클래스를 예시로 살펴보자.

```java
// 구체적인 Point 클래스

public class Point {
    // 필드값이 public으로 오픈
    public double x;
    public double y;
}
```

```java
// 추상적인 Point 클래스

public interface Point {
    // 필드값이 외부로 노출되어 있지 않고 오직 getter / setter로만 접근 가능
    double getX();
    double getY();
    void setCartesian(double x, double y);

    double getR();
    double getTheta();
    void setPolar(double r, double theta);
}
```

두번째 클래스에서 우리가 알 수 있는 사실은, 점이 직교좌표계를 사용하는지 극좌표계를 사용하는지 알 길이 없다는 점이다.
하지만 그럼에도 인터페이스는 직교좌표계/극좌표계에 대한 내용을 getter/setter로 명확히 표현한다.

두 번째 클래스의 경우 이는 자료구조 이상을 표현한다. 클래스 메서드가 접근 정책을 강제하기 때문이다. 예컨대 좌표를 읽을 때는 각 값을 개별적으로 가져오는 반면(`get()`), 좌표를 설정(`set()`)할 때는 두 값을 한 번에 설정해야 한다. setter 함수가 그렇게 하도록 해놨기 때문이다.

변수 사이에 함수라는 계층을 넣는다고 구현이 저절로 감춰지지 않는다. 구현을 감추려면 추상화가 필요하다. 단순히 getter/setter를 쓴다고 클래스가 되는 게 아니다. 그보다 추상 인터페이스를 제공해 사용자가 구현을 모른 채 자료의 핵심을 조작할 수 있어야 한다.

다시 아래 예시를 보자. 아래 두 인터페이스는 자동차의 연료 상태를 구체적인 숫자 값으로 알려준다. 

첫번째는 자동차 연료 상태를 백분율이라는 추상적인 개념으로 알려준다.

```java
public interface Vehicle {
    double getPercentFuelRemaining();
}
```

반면, 두 번째는 갤런 단위로 디테일하게 읽어서 반환하고 있음을 메소드 이름을 통해 알 수 있다.

```java
public interface Vehicle {
    double getFuelTankCapacityInGallons();
    double getGallonsOfGasoline();
}
```

첫번째는 정보가 어디서 오는지 전혀 드러나지 않는다. 하지만 두번째는 두 함수 각각 변수값을 읽어 그대로 반환할 뿐이다. 사실 사용자가 알고 싶은 건 `그래서 몇프로나 남았는데?` 임에도 불구하고 말이다.

이렇듯 자료를 세세하게 공개하기보다는 추상적인 개념으로 표현하는 것이 좋다. 인터페이스나 getter/setter 만으로 추상화가 이뤄지지 않는다. 그랬다면 바로 위 예시에서 남은 기름을 표현하는 두 가지 방법 모두 인터페이스와 getter 함수를 썼기 때문이다.

결국 개발자는 객체가 포함하는 자료를 표현할 가장 좋은 방법을 심각하게 고민해야 한다. 아무 생각 없이 getter/setter를 남발하는 방법이 가장 나쁘다.

## 자료/객체 비대칭