package it.polimi.ingsw.exception;

public class GameAlreadyStartedException extends Exception {
    public GameAlreadyStartedException() { super(); }
    public GameAlreadyStartedException(String message) { super(message); }
    public GameAlreadyStartedException(String message, Throwable cause) { super(message, cause); }
    public GameAlreadyStartedException(Throwable cause) { super(cause); }
}
