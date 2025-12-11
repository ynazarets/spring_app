package mate.academy.app.exception;

public class EmptyShoppingCartException extends RuntimeException {
    public EmptyShoppingCartException(String message) {
        super(message);
    }
}
