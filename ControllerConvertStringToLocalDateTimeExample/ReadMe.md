# Spring Controller에서 String -> 날짜 타입 자동으로 변환하기(Feat. @DateTimeFormat 적용 안되는 이유 & JAVA에서 JSON을 변환하는 과정)

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

### UpdateDateService: String -> Timestamp 변환 로직 추가

```java
@Transactional
    public void updateCurrentDateWithString(long id, String dateAsString) {

        LocalDateTime ldt =  LocalDateTime.from(df.parse(dateAsString));
        LocalDateTime nowDate = LocalDateTime.now();

        if (ldt.isAfter(nowDate)) {
            logger.error("The fetched date {} is later than current date: {}", ldt, nowDate);
            throw new IllegalArgumentException("fetchDate is later than current Date.");
        }

        DateEntity dateEntity = dateEntityRepository.findById(id).orElseThrow(() -> new DateResourceNotFoundException("There's not entity."));
        dateEntity.updateDate(ldt);
    }
```

그런데 이렇게 리뷰가 들어왔다.

> Can't we use LocalDateTime or ZonedDateTime as a type?
>

어떻게 하면 좋을지 고민해보자.

## @DateTimeFormat 적용하기: Fail..

처음에는 [`DTO에 @DateTimeFormat을 적용하면 된다더라!`는 내용의 글](https://m.blog.naver.com/PostView.naver?isHttpsRedirect=true&blogId=varkiry05&logNo=221736856257)
을 보고서는 적용했더니 제대로 동작하지 않았다. 분명히 글에서 말하기를, 클라이언트 -> 서버로 받을 때는 DateTimeFormat을 적용하면 된다고 했거늘...


```java
@Getter
@Value
public class UpdateDateRequestWithoutString {
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dateWithLDT;
}

```
왜 안됐는지는 밑에서 알아보도록 하고, 일단 다음 스텝으로 넘어가자.

## @JsonFormat 적용: Success!

계속해서 서치한 결과, [`@DateTimeFormat`이 아닌 `@JsonFormat`을 적용해야 한다는 글](https://yangbox.tistory.com/42)을 발견했다.
실제로 적용했더니, 잘 동작하는 것을 확인했다.

```java
@Getter
@Value
public class UpdateDateRequestWithoutString {
    //JsonFormat: 자체적으로 변환해준다.
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy hh:mm:ss a", timezone = "Asia/Seoul")
    private LocalDateTime dateWithLDT;
}
```

그렇다면 왜 `@DateTimeFormat`은 동작하지 않고 `@JsonFormat`은 동작했으며, 이놈들의 동작 원리는 무엇인지 살펴보도록 하자.

## JSON은 뭐고 파싱은 왜 필요하지?

먼저 JSON이란 무엇인지부터 짧게 알아보자. JSON(JavaScript Object Notation)은 데이터를 표시하는 방법 중 하나로, 이름 그대로 
자바스크립트에서 객체를 표기할 때 쓰는 텍스트 기반의 표기법이다. 이름에 자바스크립트가 들어가서 그렇지, 자바스크립트에 전혀 종속되어 있지 않은 그저 텍스트 형식 중 하나이다. 그런데 그 특성상 사람도, 컴퓨터도 보고 이해하기 쉬울 뿐만 아니라 데이터 크기가 다른 교환 형태에 비해 작아 유리하다는 이점이 있다. 구분하는 방법이 중괄호`{}`와 쌍따옴표`""`, 그리고 쉼표`,` 이 세 가지로 끝나기 때문. 이로 인해 오늘날에는 
자바스크립트 내에서뿐만 아니라 언어를 막론하고 데이터 교환의 표준으로 자리잡았다.

중괄호가 여러 겹으로 겹쳐져있는 구조 상 JSON 자체 타입이 있을 것처럼 보이지만 사실상 모양만 이럴 뿐 텍스트 형태이다. 그렇기에 어느 언어에서든 이를 받아서 사용할 수 있는 것인데, 
문제는 형태만 텍스트일 뿐, 그 내용물은 객체이기에 텍스트 -> 객체 형태로 변환해줘야 한다.. 이를 위해 JSON을 객체 형식으로 읽어들일 수 있도록 텍스트 데이터를 파싱하는 작업이 각 언어마다 필요하다. 물론 이를 엔지니어가 직접 구현할 수도 있겠으나... 얼마나 귀찮을 지 예시를 살펴보자.

만약 자바에서 JSON 데이터를 엔지니어가 직접 만든다고 해보자. 아래와 같은 엔티티가 있다.

```java
public class Student { 
    private String name; 
    private Integer classNumber; 
    
    public Person(String name, String job) { 
        this.name = name; 
        this.classNumber = classNumber; 
    } 
    
    public String getName(){ 
        return name;  
    } 
    
    public Integer getClassNumber() { 
        return classNumber;
    }
}

public static void main(String[] args) { 
    Student student = new Student("Woony", 1);
}
```
여기서 엔티티 정보를 JSON 데이터로 클라이언트에 보낸다고 해보자.

```java
String JSON = "\"{"+
    "\"name\":\"" + student.getName() + "\","+ 
    "\"job\": \"" + student.getClassNumber() + "\""+
    "}\"";
```
... 그만 알아보자.

이처럼 굉장히 귀찮을 것이므로, 웬만하면 각 언어에서 표준 라이브러리를 제공하고 있다.그렇다면 자바에서는 어떤 라이브러리가 이를 제공할까? 바로 `Jackson`이다.

## Jackson: 자바에서 JSON data 파싱을 돕는 라이브러리!

[Jackson](https://github.com/FasterXML/jackson)은 Java에서 제공하는 JSON 파싱 라이브러리이다. 
사실 꼭 JSON뿐만 아니라 XML, CSV 등 여러 데이터 포맷을 가공할 수 있는 툴이라고 보면 된다. 
너무 깊게 들어갈 필요는 없고, 이 글의 목적인 JSONFormat의 동작 원리와 제일 처음 사용했던 @DateTimeFormat이 왜 동작하지 않았는지까지의 범위 내에서만 살펴보도록 하자.

다시 처음으로 돌아가 맨 처음에 클라이언트로 데이터를 받을 떄 컨트롤러가 어떤 식으로 받는지부터 살펴보자.

```java
@PutMapping("/updateDateWithoutString/{dateId}")
    public String updateDateWithoutString(@PathVariable long dateId, @RequestBody UpdateDateRequestWithoutString request) {
        LocalDateTime fetchDate = request.getDateWithLDT();
        updateDateService.updateCurrentDateWithoutString(dateId, fetchDate);
        return "Success";
    }
```

JSON String 형태로 들어왔을 날짜값을 컨트롤러에서는 곧바로 DTO인 `UpdateDateRequestWithoutString`으로 받아주고 있다. 이때 앞에는 `@RequestBody`라는 어노테이션이 붙어있는 것을 볼 수 있다.

SpringBoot에서는 컨트롤러에서 `@RequestBody` 어노테이션을 선언하면 이렇게 말해주는 것과 같다.

> 스프링부트야, 요 루트로 들어오는 애는 JSON String이니까 객체로 변환해줘야 돼, 알았지?

이를  `@RequestBody`의 doc에서는 이렇게 설명한다.

> Annotation indicating a method parameter should be bound to the body of the web request. 
> The body of the request is passed through an `HttpMessageConverter` to resolve the method argument 
> depending on the content type of the request.
> 
> [이 어노테이션은 메소드의 파라미터가 Request의 body에 반드시 바인딩되어있어야 함을 나타낸다. Request의 body는 `HttpMessageConverter`로 넘어가는데, 
> 이 converter는 해당 request의 콘텐츠 타입에 따라 메소드 인자를 어떻게 처리할 것인지를 결정한다.]

클라이언트로부터 JSON String을 받아오면 스프링부트는 이를 HttpMessageConverter에 넘겨 JSON -> 객체 형태로 변환(역직렬화[Deserialization])한다.
이 과정에서 Jackson 라이브러리가 사용되는 것.

문제는, Jackson 라이브러리가 누구의 것이냐(==어디에 종속되어 있는가)는 거다. 지금까지 전체 틀은 스프링부트에서 진행되는 일이었으나,
정작 파싱이 진행되는 단계에서 Jackson은 스프링부트를 바라보지(==의존하지) 않는다. Jackson은 순수한 자바 라이브러리이기 때문.

당장 Jackson 라이브러리 내 아무 어노테이션이나 import해보자. Jackson 패키지는 spring framework에 들어있지 않음을 알 수 있다.

```java
import com.fasterxml.jackson.annotation.JsonFormat;
// Jackson 라이브러리 내 JsonFormat: Spring에 의존하지 않음.
```

그런데 우리가 맨 처음에 사용했던 `@DateTimeFormat`은 누구에 종속된 어노테이션일까? 바로 import해보자.

```java
import org.springframework.format.annotation.DateTimeFormat;
//DateTimeFormat: spring framework에 종속!
```

이제야 알았다. 스프링부트에서는 파싱 작업을 맡기기 위해 Jackson 라이브러리를 알아야 하지만, 정작 해당 작업을 수행하는 Jackson은 spring을 알지 못한다.
그런데 Jackson한테 알아서 날짜 파싱해달라고 일을 맡기려고 하면서 Jackson 입장에서는 자기가 모르는 도구를 쥐어주니 역직렬화에 실패하는 것.

실제로 `@DateTimeFormat`을 적용할 경우 아래와 같이 에러가 발생한다.

```java
JSON parse error: Cannot deserialize value of type `java.time.LocalDateTime` from String "2018-12-15 10:11:22"
```

반면 `@JsonFormat`은 Jackson 라이브러리 내에서 제공하는 어노테이션이다. 당연히 Jackson이 잘 아는 도구이니 변환이 쉽게 되는 것. 
## 결론

- 날짜 타입의 데이터를 클라이언트 → 서버로 받을 때 String으로 받아서 변환하는 로직을 따로 짤 필요 없이 어노테이션만으로 자동으로 바인딩받을 수 있다.
- 스프링부트에서 JSON String을 객체 형태로 변환(==역직렬화, Deserialization)하는 주체는 자바 JSON 파싱 라이브러리인 Jackson이다.
  - `헷갈리지 말 것! JSON 파싱 일을 맡기는 건 스프링부트이고 파싱 작업을 수행하는 주체는 Jackson임!`
- 참고로 기본으로 사용하는 날짜 패턴(`yyyy-MM-ddTHH:mm:ss`)의 경우 별도의 어노테이션을 달지 않아도 자동으로 바인딩(==역직렬화)된다.
- 하지만 그 이외의 형태는 `@JsonFormat` 을 적용한다. 이는 Jackson에서 제공하는 어노테이션이다. 반면 스프링 프레임워크에서 제공하는 `@DateTimeFormat`을 쓸 경우 
역직렬화에 실패한다. 왜냐면 자바 라이브러리인 Jackson은 스프링을 전혀 모르고(==의존하지 않기) 있기 때문이다.
- 즉, @DateTimeFormat은 스프링에서 제공하는 어노테이션이나, 데이터 파싱에 사용되는 자바 라이브러리인 Jackson은 정작 @DateTimeFormat을 바라보지 않아 직렬화에 실패한다.

## Reference

[https://yangbox.tistory.com/42](https://yangbox.tistory.com/42)

[https://m.blog.naver.com/PostView.naver?isHttpsRedirect=true&blogId=varkiry05&logNo=221736856257](https://m.blog.naver.com/PostView.naver?isHttpsRedirect=true&blogId=varkiry05&logNo=221736856257)

[https://jojoldu.tistory.com/361](https://jojoldu.tistory.com/361)

[http://jason-heo.github.io/programming/2021/01/23/jackson-custom-timestamp-format.html](http://jason-heo.github.io/programming/2021/01/23/jackson-custom-timestamp-format.html)