# Controller에서 날짜 자동으로 변환하기(Feat. @DateTimeFormat 적용 안되는 이유)

## Introduction

> 모든 예제는 [github](https://github.com/woonys/blog-code/tree/SpringBootExample/ControllerConvertStringToLocalDateTimeExample)에 올려뒀습니다
>

예전에 했던 작업에서 클라이언트로부터 날짜값을 받는데 쓰는 requestDTO를 만들었다. 날짜값이 클라이언트로부터 String 타입으로 들어오기에 requestDTO에서는 String으로 받고 서비스 로직에서 String을 Timestamp로 변환하도록 로직을 짰다.

### UpdateDateRequestWithString

```java
package com.woony.core.domain.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.
import lombok.NoArgsConstructor;

@Valid
@Getter
public class UpdateDateRequestWithString {
    private String dateAsString;
}
```

그런데 이렇게 리뷰가 들어왔다.

> Can't we use LocalDateTime or ZonedDateTime as a type?
>

어떻게 하면 좋을지 고민해보자.

## @DateTimeFormat 적용하기: fail

관련해서 검색했을 때, DTO에 @DateTimeFormat을 적용했더니 제대로 동작하지 않았다. 이에 대해 알아본 결과,

## @JsonFormat: Jackson에서 제공하는 어노테이션!

## 스프링부트에서 JSON 파싱하는 과정: Jackson

## 결론

- 날짜 타입의 데이터를 클라이언트 → 서버로 받을 때 String으로 받아서 변환하는 로직을 따로 짤 필요 없이 어노테이션만으로 자동으로 바인딩받을 수 있다.
- 기본으로 사용하는 날짜 패턴(`yyyy-MM-ddTHH:mm:ss`)의 경우 별도의 어노테이션을 달지 않아도 자동으로 바인딩된다.
- 하지만 그 이외의 형태는 `@JsonFormat` 을 적용한다.
- @DateTimeFormat은 스프링에서 제공하는 어노테이션이나, 데이터 파싱에 사용되는 자바 라이브러리인 Jackson은 정작 @DateTimeFormat을 바라보지 않아 직렬화에 실패한다.

## Reference

[https://yangbox.tistory.com/42](https://yangbox.tistory.com/42)

[https://m.blog.naver.com/PostView.naver?isHttpsRedirect=true&blogId=varkiry05&logNo=221736856257](https://m.blog.naver.com/PostView.naver?isHttpsRedirect=true&blogId=varkiry05&logNo=221736856257)

[https://jojoldu.tistory.com/361](https://jojoldu.tistory.com/361)

[http://jason-heo.github.io/programming/2021/01/23/jackson-custom-timestamp-format.html](http://jason-heo.github.io/programming/2021/01/23/jackson-custom-timestamp-format.html)