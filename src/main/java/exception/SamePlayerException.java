package exception;

public class SamePlayerException extends Exception {
    public SamePlayerException() { super(); }
    public SamePlayerException(String message) { super(message); }
    public SamePlayerException(String message, Throwable cause) { super(message, cause); }
    public SamePlayerException(Throwable cause) { super(cause); }
}
