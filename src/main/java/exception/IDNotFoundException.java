package exception;

public class IDNotFoundException extends Exception {
    public IDNotFoundException() { super(); }
    public IDNotFoundException(String message) { super(message); }
    public IDNotFoundException(String message, Throwable cause) { super(message, cause); }
    public IDNotFoundException(Throwable cause) { super(cause); }
}
