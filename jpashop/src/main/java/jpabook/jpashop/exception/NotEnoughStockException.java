package jpabook.jpashop.exception;

public class NotEnoughStockException extends RuntimeException {
    // 다 오버라이드 하는 이유? -> 근원적인 exception에 대해 trace가 나올 수 있게 하기 위해!
    public NotEnoughStockException() {
        super();
    }

    public NotEnoughStockException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotEnoughStockException(Throwable cause) {
        super(cause);
    }

    protected NotEnoughStockException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public NotEnoughStockException(String message) {
        super();
    }
}
