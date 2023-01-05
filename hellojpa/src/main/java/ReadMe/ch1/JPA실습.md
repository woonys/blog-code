본격적으로 JPA 실습에 들어가보자. 해당 예제는 Github(링크)에 올려뒀다. (아래는 김영한님 강의인 **[자바 ORM 표준 JPA 프로그래밍 - 기본편](https://www.inflearn.com/course/ORM-JPA-Basic/dashboard)에서 공부한 내용을 정리한 것입니다.)**

## H2 데이터베이스 설치 & 실행

해당 예제를 작업하기 위해서는 H2 데이터베이스가 필요하다. H2는 실습용으로 쓰기 위한 인메모리 DB이다. [링크(클릭)](http://www.h2database.com/html/main.html)를 누르면 해당 페이지로 이동할 수 있다. 우리는 1.4.199 버전을 사용할 것이니 해당 버전을 다운받도록 하자.

## 프로젝트 생성

이어서 프로젝트를 생성한다. IntelliJ 상단 바에서 File → New → Project를 클릭하면 아래와 같은 창이 뜬다.

![img.png](img.png)

우리 프로젝트 스펙은 위와 같이 설정하도록 하자.

- 자바 8 이상
- Maven(강의에서 Maven을 이용했기에 여기서도 똑같이 적용한다.)
- 이름은 알아서 짓자.

### 라이브러리 추가 - pom.xml

메이븐으로 새 프로젝트를 생성하면 프로젝트 디렉토리 내에 pom.xml 파일이 생성된 것을 확인할 수 있다. 의존성 추가를 위해 pom.xml에 아래의 코드를 추가한다. 각각 hibernate와 h2 데이터베이스에 대한 라이브러리 의존성을 추가하는 코드이다.

```xml
<dependencies>
		<!-- JPA 하이버네이트 -->
		<dependency>
				<groupId>org.hibernate</groupId>
				<artifactId>hibernate-entitymanager</artifactId>
				<version>5.3.10.Final</version>
		</dependency>
		<!-- H2 데이터베이스 -->
		<dependency>
				<groupId>com.h2database</groupId>
				<artifactId>h2</artifactId>
				<version>1.4.199</version>
		</dependency>
</dependencies>
```

### JPA 설정하기 - persistence.xml

이어서 JPA 설정 파일인 persistence.xml을 추가한다. 해당 파일을 인식할 수 있도록 /META-INF/persistence.xml 에 위치시키자.

![img_1.png](img_1.png)
xml 내용은 아래와 같다.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<persistence version="2.2"
             xmlns="http://xmlns.jcp.org/xml/ns/persistence" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/persistence http://xmlns.jcp.org/xml/ns/persistence/persistence_2_2.xsd">
  <persistence-unit name="hello">
    <properties>
      <!-- 필수 속성 -->
      <property name="javax.persistence.jdbc.driver" value="org.h2.Driver"/>
      <property name="javax.persistence.jdbc.user" value="sa"/>
      <property name="javax.persistence.jdbc.password" value=""/>
      <property name="javax.persistence.jdbc.url" value="jdbc:h2:~/Desktop/woonys_github/blog-code/hellojpa"/>
      <property name="hibernate.dialect" value="org.hibernate.dialect.H2Dialect"/>
      <!-- 옵션 -->
      <property name="hibernate.show_sql" value="true"/>
      <property name="hibernate.format_sql" value="true"/>
      <property name="hibernate.use_sql_comments" value="true"/>
<!--      <property name="hibernate.hbm2ddl.auto" value="create" />-->
    </properties>
  </persistence-unit>
</persistence>
```

여기서 <property> 항목을 보면 어떤 건 javax.persistence로, 다른 건 hibernate.로 시작하는 것을 볼 수 있는데, 무슨 차이일까?

- javax.persistence는 JPA 표준을 가리킨다. 따라서 JPA 표준 속성임을 뜻한다.
- hibernate는 JPA 표준 인터페이스를 구현하는 구현체 중 하나이다. 즉, hibernate로 시작하는 건 hibernate 전용 속성임을 일컫는다.

즉, JDBC 드라이버, 유저 아이디 및 패스워드 등은 JPA에서 관리하는 필수 속성이며, 디테일한 SQL 구현 관련 속성은 hibernate에서 가져오는 것임을 알 수 있다.

### 데이터베이스 방언

계속 언급하는 내용이지만, JPA는 자바 표준 ORM 인터페이스이며, 이를 구현하는 구현체는 저마다 다르다. JPA는 구현체 뿐만 아니라 특정 데이터베이스에도 역시 종속되지 않는데, 따라서 어떤 DB와도 연결할 수 있다는 장점이 있다. 그런데 같은 관계형 DB더라도 SQL 표준 문법 이외에 각각의 DB마다 제공하는 SQL 문법, 함수가 조금씩 다르다. 이렇게 각 DB에 대한 SQL 문을 방언(Dialect)라고 한다. 그래서 JPA는 어떤 DB든 그에 해당하는 SQL문을 인식해야 한다. JPA의 구현체 하이버네이트는 각 DB 별로 서로 다른 SQL 방언을 읽어들일 수 있게 세팅되어 있다. 위의 persistence.xml 파일에 보면 `<property name="hibernate.dialect" value="org.hibernate.dialect.H2Dialect"/>` 부분이 있는데, 바로 H2의 방언을 인식하기 위해 추가한 속성이다.

![img_2.png](img_2.png)
## 애플리케이션 개발