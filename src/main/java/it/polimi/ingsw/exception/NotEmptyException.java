package it.polimi.ingsw.exception;

public class NotEmptyException extends Exception {
    public NotEmptyException() { super(); }
    public NotEmptyException(String message) { super(message); }
    public NotEmptyException(String message, Throwable cause) { super(message, cause); }
    public NotEmptyException(Throwable cause) { super(cause); }
}
