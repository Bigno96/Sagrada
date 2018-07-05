package it.polimi.ingsw.exception;

public class NotEnoughFavorPointsException extends Exception {
    public NotEnoughFavorPointsException() { super(); }
    public NotEnoughFavorPointsException(String message) { super(message); }
    public NotEnoughFavorPointsException(String message, Throwable cause) { super(message, cause); }
    public NotEnoughFavorPointsException(Throwable cause) { super(cause); }
}
