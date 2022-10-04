## 자바에서 null과 자바에서 null과 빈 문자열("") 데이터의 크기는 얼마나 될까?

같은 회사 동료분과 이야기 나누다가 한 가지 질문을 받았다.

> 자바에서는 빈 문자열과 null의 크기를 어떻게 정할까요?

어라. 한 번도 생각해보지 못했다. `빈 문자열이면 당연히 크기가 0이지 않나?` 하겠지만 
데이터의 크기가 0이면 애초에 표시 자체를 할 수 없으니 말이 되지 않다. 
그 자체를 표현하기 위한 데이터의 크기가 분명 존재할 것인데, 
자바 자체적으로 정해놓은 크기가 있을 것이다.

찾아보기 전에 직접 테스트를 돌려보자. String 객체를 생성하기 전후로 힙 메모리의 용량 변화를 출력하는 코드이다. 
(아래 코드는 [해당 링크](https://www.infoworld.com/article/2077496/java-tip-130--do-you-know-your-data-size-.html)에서 가져왔다. 옛날 자바 버전이라 요즘과 결과가 다르니 로직만 참고할 것.)


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


## 왜 빈 String 객체는 크기가 24바이트일까?

먼저 우리는 String이라고 하는 애가 어떤 구조로 만들어져있는지 알 필요가 있다. String이라는 놈도 결국은 Object에서 파생된 객체이니, 
Object가 어떻게 생겼는지부터 보자.

JVM의 모든 객체는 Object에서 파생된다. 이 Object는  
1) object header와 
2) object variable을 가진다.

딱 봐도 variable은 객체에 들어가는 실제 값을 의미할텐데, 그러면 header에는 무엇이 들어갈까?

### Object header의 구성 요소

object header에는 헤더라는 애의 특성에 맞게 메타데이터가 들어있다. 정확히 말하면 메타데이터를 가리키는 포인터가 들어있는데, 각각 인스턴스와 클래스의 메타 데이터를 가리킨다.
1. Mark Word: 인스턴스의 메타데이터를 가리키는 포인터로, 해시코드를 포함한다. 64비트 시스템 기준으로 8바이트에 해당.
2. Klass Word: 클래스의 메타데이터를 가리키는 포인터, 얘는 4바이트.
3. Padding: 헤더를 감싸는 패딩으로 4바이트를 차지한다.

따라서 헤더의 크기만 총 16바이트에 해당한다.



