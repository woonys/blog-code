# 5장 - 형식 맞추기

## 형식을 맞추는 목적

> 원래 코드는 사라질지라도 개발자의 스타일과 규율은 사라지지 않는다.

오늘 구현한 기능이 다음 버전에서 바뀔 확률은 아주 높다. 이때 
오늘 구현한 코드의 가독성은 앞으로 바뀔 코드의 품질에 지대한 영향을 미친다.
오랜 시간이 지나 원래 코드의 흔적을 더 이상 찾아보기 어려울 정도로
코드가 바뀌어도 맨 처음 잡아놓은 구현 스타일과 가독성 수준을 유지보수 용이성과
확장성에 계속 영향을 미친다.

## 1. 적절한 행 길이를 유지하라

> 큰 파일보다 작은 파일이 이해하기 쉽다

소스 코드(클래스가 아닌 파일 전체에서) 길이는 얼마가 적당할까?
JUnit, FitNesse 등 유명한 프로그램을 포함해 대다수 자바 소스 파일을 조사한 결과 
- 한 파일에 200줄 미만이 가장 많았다.
- 500줄 이상 넘어가는 파일은 거의 없었다.

즉, 우리는 500줄을 넘지 않고 대부분 200줄 정도인 파일로도 커다란 시스템을 만들 수 있다.

### 신문기사처럼 작성하기
가장 좋은 방법은 신문 기사처럼 작성하는 것이다. 여기서 말하는 신문 기사의 특징은 아래와 같다.

- 파일 이름은 간단하면서도 설명이 가능하게 짓는다(제목짓기와 동일)
- 파일의 첫 부분은 고차원 개념과 알고리즘을 설명한다(첫 문단에서 전체 기사를 요약하듯)
- 아래로 내려갈수록 의도를 세세하게 묘사한다(주장을 설명하는 디테일한 근거)

### 개념은 빈 행으로 분리하라

> 일련의 행 묶음은 완결된 생각 하나를 표현한다.

글에서 단락이 끝나면 다음 단락으로 넘어갈 때 한 칸 띄우듯, 소스 코드도 마찬가지로 작성하자.
아래 예시는 그렇게 쓰지 않았을 경우 가독성이 얼마나 떨어지는지를 보여준다.

**행 포함 O**
```java
package com.example.cleancode.cleancodestudy.Ch5;

import java.util.regex.*;

public class BoldWidget {
    public static final String REGEXP = "'''.+?'''";
    private static final Pattern pattern = Pattern.compile("'''(.+?)'''", Pattern.MULTILINE + Pattern.DOTALL
    );
    
    public BoldWidget(ParentWidget parent, String text) throws Exception {
        super(parent);
        Matcher match = pattern.matcher(text);
        match.find();
        addChildWidgets(match.group(1));
    }
    
    public String render() throws Exception {
        StringBuffer html = new StringBuffer("<b>");
        html.append(childHtml()).append("</b>");
        return html.toString();
    }
}
```

**행 포함 X**

```java
package com.example.cleancode.cleancodestudy.Ch5;
import java.util.regex.*;
public class BoldWidget {
    public static final String REGEXP = "'''.+?'''";
    private static final Pattern pattern = Pattern.compile("'''(.+?)'''", Pattern.MULTILINE + Pattern.DOTALL);
    public BoldWidget(ParentWidget parent, String text) throws Exception {
        super(parent);
        Matcher match = pattern.matcher(text);
        match.find();
        addChildWidgets(match.group(1));}
    public String render() throws Exception {
        StringBuffer html = new StringBuffer("<b>");
        html.append(childHtml()).append("</b>");
        return html.toString();
    }
}
```

### 세로 밀집도

서로 밀접한 코드 행은 세로로 가까이 놓여야 한다.

### 수직 거리

서로 밀접한 개념은 세로로 가까이 둬야 한다. 타당한 근거가 없다면 서로 밀접한 개념은 한 파일에 속해야 하며
한 파일 안에서도 서로 가까이 둬야 한다. 

**변수 선언** 역시 사용하는 위치에 최대한 가까이 선언한다. 
반면 인스턴스 변수는 얘기가 다르다. 클래스 맨 처음에 선언하며
변수 간에 거리를 두지 않는다. 잘 설계한 클래스는 많은 메서드가 해당 변수를 사용하기 때문이다.

**종속 함수** 에서도 수직 거리는 짧아야 한다. 한 함수가 다른 함수를 호출하면 두 함수는 세로로
가까이 배치할수록 자연스럽게 읽힌다.

**개념적 유사성**이 가까운 코드일수록 역시 서로 가까이 배치한다. 친화도에는 여러 요인이 있다.
위의 종속 함수는 개념적 유사성의 한 예시에 해당한다.

### 세로 순서

함수 호출시 종속성은 아래 방향으로 유지한다. 즉, 호출되는 함수가 호출하는 함수보다 아래에 있는게 자연스럽다.
이렇게 하면 소스 코드 모듈이 고차원에서 저차원으로 자연스럽게 내려간다. 앞에서 신문기사의 비유를 들었듯, 
중요한 핵심 내용이 먼저 오고 이를 부연하는 근거 및 디테일한 설명이 뒤이어 오는 것을 생각해보면 알 수 있다.

## 2. 가로 형식 맞추기
> 프로그래머는 명백히 짧은 행을 선호한다.

세로 길이는 500자를 넘지 않고 200자 내외면 적당하다는 것을 알았다. 그러면 가로 길이는 어떨까?

일반적인 프로그램에서 행 길이를 조사한 결과, 45자 전후가 좋고 80자 이후부터는 급격히 떨어지는 것을 볼 수 있다.
그러므로 짧은 행이 바람직하다. 물론 요즘에는 모니터가 좋아서 120자를 넘어도 나쁘지 않다. 200자까지도 한 화면에 들어온다.
하지만 가급적 120자를 넘기지 않도록 하자.

### 가로 공백과 밀집도

> 가로로는 공백을 사용해 밀접한 개념과 느슨한 개념을 표현한다.

아래 예시에서는 할당 연산자를 강조하려고 앞뒤에 공백을 줬다.
공백을 넣으면 두 가지 주요 요소가 확실히 나뉜다는 사실이 더욱 분명해진다.
반면 함수 이름과 이어지는 관호 사이에는 공백을 넣지 않았다.
함수와 인수는 서로 밀접하기 때문이다.

```java
private void measureLine(String line) {
    lineCount();
    int lineSize = line.length():
    totalChars += lineSize;
    lineWidthHistogram.addLine(lineSize, lineCount);
    recordWidestLine(lineSize);
}


```

### 들여쓰기

소스파일에서 범위로 이뤄진 계층을 표현하기 위해 코드를 들여쓴다. 들여쓰는 정도는 계층에서
코드가 자리잡은 수준에 비례한다. 
- 클래스 정의처럼 파일 수준인 문장은 들여쓰지 않는다.
- 클래스 내 메서드는 클래스보다 한 수준 들여쓴다. 
- 메서드 코드는 메서드 선언보다 한 수준 들여쓴다. 
- 블록 코드는 블록을 포함하는 코드보다 한 수준 들여쓴다.

들여쓰기한 파일은 구조가 한눈에 들어온다. 변수, 생성자 함수, 접근자 함수, 메서드가 금방 보인다.

## 팀 규칙

팀에 속한다면 자신이 선호해야 할 규칙은 팀 규칙이다. 우리 회사 서버팀 역시 서버 코딩 컨벤션이 존재하며 최대한 해당
컨벤션에 맞춰서 작업할 것을 권고한다.

## 결론

좋은 소프트웨어 시스템은 읽기 쉬운 문서로 이뤄진다는 사실을 기억해라. 스타일은 일관적이고 매끄러워야 하낟.
온갖 스타일을 뒤섞어 소스코드를 필요 이상으로 복잡하게 만드는 실수는 반드시 피한다.