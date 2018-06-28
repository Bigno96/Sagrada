package it.polimi.ingsw.exception;

public class NotTurnException extends Exception {
    public NotTurnException() { super(); }
    public NotTurnException(String message) { super(message); }
    public NotTurnException(String message, Throwable cause) { super(message, cause); }
    public NotTurnException(Throwable cause) { super(cause); }
}
