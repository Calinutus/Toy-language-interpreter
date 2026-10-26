package interpreter.exceptions;

public class StatementException extends RuntimeException {
    public StatementException(String message) {
        super(message);
    }
}
