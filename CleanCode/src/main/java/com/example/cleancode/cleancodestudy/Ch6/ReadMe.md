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

두번째 클래스에서 우리가 알 수 있는 사실은, 점이 직교좌표계를 사용하는지 극좌표계를 사용하는지 알 길이 없다는 점이다. 하지만 그럼에도 인터페이스는 직교좌표계/극좌표계에 대한 내용을 getter/setter로 명확히 표현한다.

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

첫번째는 정보가 어디서 오는지 전혀 드러나지 않는다. 하지만 두번째는 두 함수 각각 변수값을 읽어 그대로 반환할 뿐이다. 사실 사용자가 알고 싶은 건 `그래서 몇프로나 남았는데?` 임에도 불구하고 말이다. 이렇듯 자료를 세세하게 공개하기보다는 추상적인 개념으로 표현하는 것이 좋다. 인터페이스나 getter/setter 만으로 추상화가 이뤄지지 않는다. 그랬다면 바로 위 예시에서 남은 기름을 표현하는 두 가지 방법 모두 인터페이스와 getter 함수를 썼기 때문이다. 

개발자는 객체가 포함하는 자료를 표현할 가장 좋은 방법을 심각하게 고민해야 한다. 아무 생각 없이 getter/setter를 남발하는 방법이 가장 나쁘다.

## 자료/객체 비대칭

처음의 Point 클래스 예시는 객체와 자료 구조 사이에 벌어진 차이를 보여준다. 객체(`interface Point`)는 추상화 뒤로 자료를 숨긴 채 자료를 다루는 함수만 제공한다. 반면, 자료구조(`class Point`)는 자료를 그대로 공개하며 별다른 함수는 제공하지 않는다.

이는 객체와 자료구조가 근본적으로 양분된다는 사실에서 비롯된다. 자료구조를 사용하는 절차적인 코드는 기존 자료구조를 변경하지 않으면서 새 함수를 추가하기 쉽다. 반면, 객체 지향 코드는 기존 함수를 변경하지 않으면서 새 클래스를 추가하기 쉽다. 아래 예시를 보자.

**절차지향적인 도형 클래스**
```java
import java.awt.Point;

public class Square {
    public Point topLeft;
    public double side;
}

public class Rectangle {
    public Point topLeft;
    public double height;
    public double width;
}

public class Circle {
    public Point center;
    public double radius;
}

public class Geometry {
    public final double PI = 3.141592;
    
    public double area(Object shape) throws NoSuchShapeException {
        if (shape instanceof Square) {
            Square s = (Square) shape;
            return s.side * s.side;
        } else if (shape instanceof Rectangle) {
            Rectangle r = (Rectangle) shape;
            return r.height * r.width;
        } else if (shape instanceof Circle) {
            Circle c = (Circle) shape;
            return PI * c.radius * c.radius;
        }
        throw new NoSuchShapeException();
    }
}
```
Geometry 클래스를 보자. area() 메소드는 상당히 절차적인 것을 볼 수 있다.
1. Shape을 인자로 던져주면
2. if 정사각형이면 -> 한 변의 길이을 제곱해라
3. else if 직사각형이면 -> 밑변 * 높이를 계산해라
4. else if 원이면 -> PI * 반지름의 제곱을 계산해라

그럼 어떤 게 객체지향적인 코드일까? 아래 예시를 보자.

**객체지향적인 도형 클래스**

```java
import java.awt.Shape;

public class Square implements Shape {
    private Point topLeft;
    private double side;

    public double area() {
        return side * side;
    }
}

public class Rectangle implements Shape {
    private Point topLeft;
    private double height;
    private double width;

    public double area() {
        return height * width;
    }
}

public class Circle {
    private Point center;
    private double radius;

    public double area() {
        return PI * radius * radius;
    }
}
```
무엇이 달라졌나? 

1. 각 객체는 Shape라는 인터페이스를 구현한 클래스로, shape에 대한 다형성을 지닌다. 
2. public으로 바깥에 노출되었던 클래스의 상태값이 private로 은닉되어 외부에서 접근이 불가능해졌다(캡슐화).
3. 이전에는 Geometry라는 클래스로 따로 빠져있던 area를 계산하는 로직이 각 클래스 내에 메소드로 들어갔다. 즉, 영역 값을 반환하는 책임이 아예 다른 클래스(Geometry)가 아닌 각 객체로 이관되었다.

객체 간 역할, 책임, 협력을 지향하는 객체지향 패러다임을 잘 드러냈다. 그런데 둘 사이에는 상반되는 장단점이 존재한다.

먼저 절차지향 코드를 보자. 만약 객체의 둘레를 계산하는 perimeter() 메소드를 추가한다고 해보자. 이때 우리는 Geometry 클래스 안에 새로운 메소드 하나만 추가하면 끝난다. 기존의 도형 클래스들은 어떤 영향도 받지 않는다. 반면, 새 도형을 추가해야 될때는 어떻게 될까? 삼각형을 하나 추가했다고 해보자.

```java
import java.awt.Point;

public class Triangle {
    public Point topLeft;
    public double base;
    public double width;
}

public class Geometry {
    public final double PI = 3.141592;
    
    public double area(Object shape) throws NoSuchShapeException {
        if (shape instanceof Square) {
            Square s = (Square) shape;
            return s.side * s.side;
        } else if (shape instanceof Rectangle) {
            Rectangle r = (Rectangle) shape;
            return r.height * r.width;
        } else if (shape instanceof Circle) {
            Circle c = (Circle) shape;
            return PI * c.radius * c.radius;
        } else if (shape instanceof Triangle) {
            Triangle t = (Triangle) shape;
            return t.base * t.width / 2;
        }
        throw new NoSuchShapeException();
    }
}
```

보다시피 기존의 area() 메소드를 고쳐야 한다. 지금이야 도형이 네 개지만, 만약 여기서 더 늘어나면? area() 메소드는 점점 유지보수가 어려워진다.

그럼 객체지향은 무조건 좋을까? 반대로 새로운 도형(클래스)를 추가하는 건 별로 어렵지 않다. 똑같이 상속받아서 하나 만들면 그만이니까. 그런데 둘레를 계산하는 새로운 메소드를 추가한다고 생각해보자.

```java
import java.awt.Shape;

public class Square implements Shape {
    private Point topLeft;
    private double side;

    public double area() {
        return side * side;
    }

    public double perimeter() {
        return 4 * side;
    }
}

public class Rectangle implements Shape {
    private Point topLeft;
    private double height;
    private double width;

    public double area() {
        return height * width;
    }

    public double perimeter() {
        return 2 * (height + width);
    }
}

public class Circle {
    private Point center;
    private double radius;

    public double area() {
        return PI * radius * radius;
    }
    
    public double perimeter() {
        return 2 * PI * radius;
    }
}
```

보는 바와 같이 메소드를 추가할 때는 객체지향이 훨씬 고쳐야할 게 많다. 해당 행동과 관련된 모든 객체에게 행동을 부여해야 하기 때문이다.

정리해보자. 절차적인 코드는 새로운 자료구조를 추가하기 어렵다. 이를 위해 모든 함수를 고쳐야 한다. 반면 객체지향 코드는 새 함수를 추가하기 어렵다. 그러려면 모든 클래스를 고쳐야 한다.

즉, 객체지향 코드에서 어려운 변경은 절차적인 코드에서 쉽고, 절차적인 코드에서 어려운 변경은 객체지향 코드에서 쉽다. 이를 알면 늘 객체지향만 고집할 것이 아니라 상황에 맞게 코드를 짤 수 있다.

예컨대 복잡한 시스템을 짜다 보면 새로운 자료 타입이 필요한 경우가 생긴다. 이때는 클래스 및 객체지향 기법을 사용하는 것이 적절하다. 반면, 새로운 함수가 필요한 경우도 생긴다. 이때는 절차적인 코드와 자료구조가 더 적합하다.

즉, 모든 것이 객체라는 생각은 미신임을 알아야 한다. 때로는 단순한 자료구조와 절차적인 코드가 가장 적합한 상황도 있다.

## 디미터 법칙

> 모듈은 자신이 조작하는 객체의 속사정을 몰라야 한다.

객체는 자료를 숨기고 함수를 공개한다. 즉, 객체는 조회 함수로 내부 구조를 공개하면 안된다.

정확히 표현하면, 디미터 법칙은, `클래스 C의 메서드 f는 아래와 같은 객체의 메소드만 호출헤애 한다`고 주장한다.

- 클래스 C
- 메소드 f로 생성한 객체
- f의 인자로 넘어온 객체
- C 인스턴스 변수에 저장된 객체

핵심은 `낯선 사람은 경계하고 친구랑만 놀라`는 의미다.

### 위반 예시: 기차 충돌

당연한 것 같지만 이를 어긴 예시를 보면 명확히 와닿는다.

`final String outputDir = ctxt.getOptions().getScratchDir().getAbsolutePath();`

우리는 모든 getter를 체인으로 연결함으로써 내부 속사정을 다 들여볼 수 있게 된다. 위와 같은 코드를 기차 충돌이라 한다. 이런 건 조잡하기에 피하는 게 좋다.
위 코드는 아래와 같이 나누는 게 좋다.

```java
Options opts = ctxt.getOptions();
File scratchDir = opts.getScratchDir()
final String outputDir = scratchDir.getAbsolutePath();
```

이렇게 나눈다고 위에서 얘기한 `getter()를 통한 내부 속사정 엿보기`가 안 드러나는 건 아니다. 그럼 위 코드는 무조건 잘못됐을까? 상황에 따라 다르다. 위 예제가 디미터 법칙을 위반하는지 여부는 ctxt, Options, ScratchDir이 객체인지 자료구조인지 여부에 따라 달렸다. 객체라면 내부 구조를 숨겨야 하고, 자료구조라면 드러나는 게 당연하다.

## 자료 전달 객체

자료 구조체의 전형적인 형태는 공개 변수만 있고 함수가 없는 클래스다. 이런 자료 구조체를 자료 전달 객체(Data Transfer Object, DTO)라고 한다. DTO는 굉장히 유용한 구조체다. 특히 DB와 통신하거나 소켓에서 받은 메시지의 구문을 분석할 때 유용하다.

흔히 DTO는 DB에 저장된 가공되지 않은 정보를 애플리케이션 코드에서 사용할 객체로 변환하는 일련의 단계에서 가장 처음으로 사용하는 구조체이다.

## 결론

- 객체는 동작을 공개하고 자료를 숨긴다. 그래서 기존 동작을 변경하지 않으면서 새 객체 타입을 추가하기는 쉬운 반면, 기존 객체에 새 동작을 추가하기는 어렵다.

- 자료 구조는 별다른 동작 없이 자료 자체를 노출한다. 그래서 기존 자료 구조에 새 동작을 추가하기는 쉬우나, 기존 함수에 새 자료 구조를 추가하기는 어렵다.

- 시스템을 구현할 때, 새로운 자료 타입을 추가하는 유연성이 필요하면 객체가 더 적합하다. 새 동작을 추가하는 유연성이 필요하면 자료구조와 절차지향 코드가 더 적합하다.

- 우수한 개발자는 편견 없이 이 사실을 이해하고 직면한 문제에 최적의 해결책을 선택한다.


