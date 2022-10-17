package kitchenpos.eatinorders.tobe.domain.exception;

public class IllegalOrderLineException extends IllegalArgumentException {

    public static final String NO_MENU = "존재하지 않는 메뉴입니다.";
    public static final String NOT_DISPLAYED = "진열 메뉴가 아닙니다.";
    public static final String PRICE_MISMATCH = "메뉴와 가격이 일치하지 않습니다.";

    public IllegalOrderLineException(String message) {
        super(message);
    }
}
