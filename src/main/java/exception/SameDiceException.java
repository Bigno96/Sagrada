package exception;

public class SameDiceException extends Exception {
    public SameDiceException() { super(); }
    public SameDiceException(String message) { super(message); }
    public SameDiceException(String message, Throwable cause) { super(message, cause); }
    public SameDiceException(Throwable cause) { super(cause); }
}
