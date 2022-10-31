# 2장 - 의미 있는 이름

## 들어가면서

이름 잘 지으면? 여러모로 편하다는 걸 명심할 것. 여기서는 이름을 잘 짓는 규칙 몇 가지에 대해서 알아보도록 하자.

## 의도를 분명히 밝혀라

- 의도가 분명한 이름이 정말로 중요하다. 
- 더 나은 이름이 떠오르면 개선해야 한다.

모든 클래스의 이름은 아래의 질문에 답변해야 한다.
- 변수/함수/클래스의 존재 이유는?
- 이들의 수행 기능은?
- 사용 방법은?

이때 주석이 필요하다면 -> 변수명을 제대로 짓지 못했다는 것(의미를 잘 반영하지 못했다는 것이니)!

예시를 보자.
```java
int d; // 경과 시간(단위: 날짜)
```
d에는 어떤 의미도 없다. 측정하려는 값과 단위를 표현하는 이름이 필요함.

```java
int elapsedTimeInDays; // 날짜에서 잰 시간
int daysSinceCreation; // 만들어진 이래로 며칠이 지났는가
int daysSinceModification; // 수정한 이래로 며칠이 지났는가
int fileAgeInDays; // 파일의 수명
```
위의 날짜명은 모두 저마다 의미를 갖고 있으며 그 의미가 분명하게 드러난다.

예시 하나를 보자.
```java
public List<int[]> getThem() {
    List<int[]> list1 = new ArrayList<int[]>();
    for (int[] x : theList) {
        if (x[0] ==4) {
            list1.add(x);
        }
    }
}
```
대체 getThem()이라는 놈의 정체가 뭘까? 뭔가(them)를 가져온다는 것까지는 알겠는데, 뭔 놈을 가져온다는 것이며 어떻게 가져오는 것인지
맥락이 전혀 드러나지 않는다. 여기에는 이 코드를 읽는 독자가 이미 알고 있을 것으로 전제하고 쓴 항목이 있는데, 아래와 같다.  

1. theList에는 무엇이 들어있는지
2. theList에서 0번째 값이 왜 중요한지 -> `x[0] == 4` 를 체크하는 이유
3. 2에서 값 4는 무슨 의미인지
4. 함수가 반환하는 리스트 list1을 어떻게 사용하는지

만약 위의 예시가 지뢰찾기 게임이었다고 해보자. 이 경우, 각 숫자 및 변수에 의미를 보다 명확하게 드러낼 수 있다. theList는 게임판이었을테니, gameBoard로 바꾼다.
배열`list1`에서 0번째 값은 칸의 상태(아직 미개척 상태인지, 깃발이 꽂혔는지 등)를 의미하며, 값 4는 깃발이 꽂힌 상태를 말한다. 이렇게 각 개념에 이름을 붙여 다시 코드를 짜보자.

```java
public List<int[]> getFlaggedCells() {
    List<int[]> flaggedCells = new ArrayList<int[]>();
    for (int[] cell : gameBoard) {
        if (cell[STATUS_VALUE == FLAGGED) {
            flaggedCells.add(cell);
        }
        return flaggedCells;
    }
}
```

여기서 몇 가지 더 추가해보자. int[] 배열을 쓰는 대신, cell 자체를 간단한 클래스로 만들어도 되겠다. isFlagged라는 더 명시적인 함수를 사용해 FLAGGED라는 상수를 감춰보자.

```java
public List<Cell> getFlaggedCells() {
    for (Cell cell : gameBoard) {
        if (cell.isFlagged())
            flaggedCells.add(cell);
    return flaggedCells;
    }
}
```

이제는 훨씬 더 이 메소드를 이해하기가 쉬워졌다. 이것이 좋은 이름이 주는 위력이다.

## 그릇된 정보를 피하라

평소에 쓰는 단어를 다른 의미로 사용하게 될 경우, 독자로 하여금 헷갈릴 여지가 높다. 예컨대 ls는 리눅스에서 현재 디렉토리에 있는 파일을 전부 보여달라는 의미를 가지고 있다. 이를 다른 의미를 갖는 변수로
사용한다면, 프로그래머에게 혼동을 줄 수 있다.