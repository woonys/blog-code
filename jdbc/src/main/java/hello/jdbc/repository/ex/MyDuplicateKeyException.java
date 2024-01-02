package hello.jdbc.repository.ex;

public class MyDuplicateKeyException extends MyDbException { // 이 예외는 데이터 중복인 경우에만 던진다.
    // 이 예외는 우리가 직접 만들었기 때문에 특정 기술에 종속적이지 않다. 따라서 서비스 계층 순수성을 유지 가능.
    public MyDuplicateKeyException() {
    }

    public MyDuplicateKeyException(String message) {
        super(message);
    }

    public MyDuplicateKeyException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyDuplicateKeyException(Throwable cause) {
        super(cause);
    }
}
