# 자바에서 null과 빈 문자열("") 데이터의 크기는 얼마나 될까?

## Introduction

같은 회사 동료분과 이야기 나누다가 한 가지 질문을 받았다.

> 자바에서는 빈 문자열과 null의 크기를 어떻게 정할까요?

어라. 한 번도 생각해보지 못했다. `빈 문자열이면 당연히 크기가 0이지 않나?` 하겠지만 
데이터의 크기가 0이면 애초에 표시 자체를 할 수 없으니 말이 되지 않다. 
그 자체를 표현하기 위한 데이터의 크기가 분명 존재할 것인데, 
자바 자체적으로 정해놓은 크기가 있을 것이다.

## 1. 빈 문자열의 크기는 얼마나 될까?  


찾아보기 전에 직접 테스트를 돌려보자. String 객체를 생성하기 전후로 힙 메모리의 용량 변화를 출력하는 코드이다. 
(아래 코드는 [해당 링크](https://www.infoworld.com/article/2077496/java-tip-130--do-you-know-your-data-size-.html)에서 가져왔다. 사이트에서 말하는 결과값은 예전 자바 버전이라 요즘과 결과가 다르니 코드만 참고할 것.)


```
public class Sizeof
{
    public static void main (String [] args) throws Exception
    {
        // Warm up all classes/methods we will use
        runGC ();
        usedMemory ();
        // Array to keep strong references to allocated objects
        final int count = 100000;
        Object [] objects = new Object [count];
        
        long heap1 = 0;
        // Allocate count+1 objects, discard the first one
        for (int i = -1; i < count; ++ i)
        {
            Object object = null;
            
            // Instantiate your data here and assign it to object
            
            object = new String ();
            
            if (i >= 0)
                objects [i] = object;
            else
            {
                object = null; // Discard the warm up object
                runGC ();
                heap1 = usedMemory (); // Take a before heap snapshot
            }
        }
        runGC ();
        long heap2 = usedMemory (); // Take an after heap snapshot:
        
        final int size = Math.round (((float)(heap2 - heap1))/count);
        System.out.println ("'before' heap: " + heap1 +
                            ", 'after' heap: " + heap2);
        System.out.println ("heap delta: " + (heap2 - heap1) +
            ", {" + objects [0].getClass () + "} size = " + size + " bytes");
        for (int i = 0; i < count; ++ i) objects [i] = null;
        objects = null;
    }
    private static void runGC () throws Exception
    {
        // It helps to call Runtime.gc()
        // using several method calls:
        for (int r = 0; r < 4; ++ r) _runGC ();
    }
    private static void _runGC () throws Exception
    {
        long usedMem1 = usedMemory (), usedMem2 = Long.MAX_VALUE;
        for (int i = 0; (usedMem1 < usedMem2) && (i < 500); ++ i)
        {
            s_runtime.runFinalization ();
            s_runtime.gc ();
            Thread.currentThread ().yield ();
            
            usedMem2 = usedMem1;
            usedMem1 = usedMemory ();
        }
    }
    private static long usedMemory ()
    {
        return s_runtime.totalMemory () - s_runtime.freeMemory ();
    }
    
    private static final Runtime s_runtime = Runtime.getRuntime ();
} 
// End of class
```
전체 로직은 다음과 같다.  
1. 워밍업으로 가비지 콜렉터를 실행한 뒤, 현재 사용 중인 메모리를 체크
2. 100,000개 길이의 Object array를 생성(String 한 개의 크기는 매우 작기 때문에 증폭시키려고 하는 듯..? 아마?) (정확히는 100,001개 길이 배열 -> 처음에 빈 Object 객체를 이니셜 용으로 만들고 바로 지워버린다. 초기화 용도라고 생각하면 됨)
3. 현재 사용 중인 메모리를 체크(heap1)
4. 100,000개 array에 빈 String 객체를 다 채워 넣는다.
5. 이때 사용 중인 메모리를 체크(heap2)
6. (heap2 - heap1) / 100,000 = 빈 String 객체 하나의 바이트 값  

결과는 어떻게 됐을까?

```
'before' heap: 749944, 'after' heap: 3150320
heap delta: 2400376, {class java.lang.String} size = 24 bytes
```

답은 `24바이트`였다. 그러면 왜 24 바이트인지 살펴보도록 하자.


## 2. 왜 빈 String 객체는 크기가 24바이트일까?

먼저 우리는 String이라고 하는 애가 어떤 구조로 만들어져있는지 알 필요가 있다. String이라는 놈도 결국은 Object에서 파생된 객체이니, 
Object가 어떻게 생겼는지부터 보자.

JVM의 모든 객체는 Object에서 파생된다. 이 Object는  
1) object header와 
2) object variable을 가진다.

variable은 객체에 들어가는 실제 값을 의미할텐데, 그러면 header에는 무엇이 들어갈까?

### Object header의 구성 요소

object header에는 헤더라는 애의 특성에 맞게 메타데이터가 들어있다. 정확히 말하면 메타데이터를 가리키는 포인터가 들어있는데, 각각 인스턴스와 클래스의 메타 데이터를 가리킨다.
1. Mark Word: 인스턴스의 메타데이터를 가리키는 포인터로, GC와 객체 lock을 걸 때 사용한다. 해시코드를 포함하고 있으며, 식별할 수도 있다. 64비트 시스템 기준으로 8바이트에 해당.
2. Klass Word: 클래스의 메타데이터를 가리키는 포인터. 얘는 4바이트.
3. Header: 자바의 모든 오브젝트들은 8바이트의 배수로 사이즈를 맞춘다. 이를 위해, 만약 (Mark + Klass) + Variable의 총합이 8의 배수로 맞아떨어지지 않을 경우 패딩이 추가된다.
따라서 헤더의 크기는 패딩이 추가되지 않는 기본 상태에서는 12바이트에 해당한다.

오케이, 헤더에는 이렇게 들어가고. 이제 남은 건 Object variable이다. 여기에는 무엇이 들어갈까?

### Object variable의 구성 요소

이를 확인하기 위해 String 클래스를 확인해보자. java.lang 패키지에서 String 클래스를 찾으면 아래와 같이 나온다.

```java

public final class String
    implements java.io.Serializable, Comparable<String>, CharSequence {
    /** The value is used for character storage. */
    private final char value[];

    /** Cache the hash code for the string */
    private int hash; // Default to 0

    /** use serialVersionUID from JDK 1.0.2 for interoperability */
    private static final long serialVersionUID = -6849794470754667710L;

    /**
     * Class String is special cased within the Serialization Stream Protocol.
     *
     * A String instance is written into an ObjectOutputStream according to
     * <a href="{@docRoot}/../platform/serialization/spec/output.html">
     * Object Serialization Specification, Section 6.2, "Stream Elements"</a>
     */
    private static final ObjectStreamField[] serialPersistentFields =
        new ObjectStreamField[0];

    /**
     * Initializes a newly created {@code String} object so that it represents
     * an empty character sequence.  Note that use of this constructor is
     * unnecessary since Strings are immutable.
     */
    public String() {
        this.value = "".value;
    }
```

이 중에서 힙 메모리에 차지하는 영역만 살펴보면, 크게 두 가지가 눈에 띈다.

1. `private final char value[];`
   - String(문자열) 클래스는 char(문자)를 배열로 만들어서 구현한다. 지금은 빈 문자열이니 char[]에는 아무 것도 들어있지 않다. 이 빈 array를 생성하는데 드는 크기는 4바이트에 해당한다.
2. `private int hash;`
   - 위의 설명에도 나와있듯, String에서 hash값은 동일한 String에 대해 새로운 메모리를 할당하지 않고 기존 힙 메모리에 저장해둔 문자열 값을 새 변수에서는 그저 참조만 하게끔 함으로써 공간 효율을 높이기 위해 쓰인다. 이는 int이므로 크기가 4바이트에 해당한다.

이렇게만 써두면 뭔가 찜찜하다..실제로 위와 동일한지 확인해보고 싶다. 마침 자바에서는 Java Object Layout(JOL)이라는 툴을 제공한다. JOL은 openjdk 프로젝트로, 자바 클래스 및 객체가 메모리에 올라갔을 때 그 구조(layout)을 분석할 수 있는 툴이다.
설치하려면 gradle에 아래와 같이 의존성을 추가한다.  

`implementation 'org.openjdk.jol:jol-core:0.16'`  

그러고 나서 아래와 같이 String 클래스를 parsing하는 코드를 실행해본다.

```java
package com.example.javanullandemptystringexample;

import org.openjdk.jol.info.ClassLayout;

public class ParseSpringClass {

    public static void main(String[] args) {
        String str = "";
        System.out.println(ClassLayout.parseInstance(str).toPrintable());
    }
}
```

출력 결과는 아래와 같다.
  

```java
java.lang.String object internals:
OFF  SZ     TYPE DESCRIPTION               VALUE
  0   8          (object header: mark)     0x0000000000000001 (non-biasable; age: 0)
  8   4          (object header: class)    0x000016d0
 12   4   char[] String.value              []
 16   4      int String.hash               0
 20   4          (object alignment gap)    
Instance size: 24 bytes
```

Object header와 variable에 위에서 얘기한 값이 들어가 있는 걸 확인할 수 있다. 어라, 근데 하나가 더 추가되어 있다. 바로 `(object alignment gap)`이라는 놈인데, 
이것이 앞서 object header에서 언급했던 padding에 해당한다. 자바의 모든 오브젝트는 8의 배수로 맞아 떨어져야 하는데, 우리가 언급한 값만 다 더하면 12(header) + 8(variable)이 끝이므로 이를 위해 4바이트를 추가해 8로 나눠 떨어지도록 맞춰준다.

## 3. null의 크기는 얼마일까?

오케이, 빈 String은 그 크기가 얼마인지 체크했다. 그렇다면 다시 처음 질문으로 돌아가보자. null은 그 크기가 어떻게 될까? String 클래스를 파싱했던 것처럼 null 클래스도 똑같이 파싱해보자.

```java
package com.example.javanullandemptystringexample;

import org.openjdk.jol.info.ClassLayout;

public class ParseNullClass {

    public static void main(String[] args) {
        Object nullObj = null;
        System.out.println(ClassLayout.parseInstance(nullObj).toPrintable());
    }
}
```

하지만 결과는 아래와 같다.

```java
Execution failed for task ':ParseNullClass.main()'.
```

이렇게 뜨는 이유는 무엇일까? 바로, null은 Object의 일종이 아니기 때문이다. 착각하면 안 되는 게, null은 객체도 자료형도 아니다. 모든 레퍼런스에 할당될 수 있는 특수한 변수로 취급될 뿐이다.
오케이, 그렇지만 여전히 메모리 한 켠에 자리를 차지하고 있을 것이기 때문에 이에 대한 자바 자체의 규약이 있을 테다. 이에 대해 찾아보니 여기 [링크: Does null variable require space in memory](https://stackoverflow.com/questions/2430655/java-does-null-variable-require-space-in-memory)
에서 말하기를 64비트 기준 8바이트를 차지한다고 한다. 이는 특별히 이유가 있어서라기보다 약속으로 정해둔 것이지 않을까 싶다. 위에서 얘기했듯 모든 자바 객체는 8바이트의 배수로 이뤄지는데, null은 객체는 아니나 어떤 객체에든 부여될 수 있는 값이기 떄문이라 그렇지 않을까 싶다.

결론적으로 null의 크기는 8바이트이다.

## Reference

[https://www.baeldung.com/java-memory-layout](https://www.baeldung.com/java-memory-layout)
[https://stackoverflow.com/questions/2430655/java-does-null-variable-require-space-in-memory](https://stackoverflow.com/questions/2430655/java-does-null-variable-require-space-in-memory)
[https://m.blog.naver.com/lestat85/220217676199](https://m.blog.naver.com/lestat85/220217676199)
[https://github.com/openjdk/jol](https://m.blog.naver.com/lestat85/220217676199)
[https://www.javamex.com/tutorials/memory/string_memory_usage.shtml](https://www.javamex.com/tutorials/memory/string_memory_usage.shtml)
[https://mavenlibs.com/maven/dependency/org.openjdk.jol/jol-core](https://mavenlibs.com/maven/dependency/org.openjdk.jol/jol-core)
[https://docs.oracle.com/javaee/7/tutorial/bean-validation002.htm](https://docs.oracle.com/javaee/7/tutorial/bean-validation002.htm)






