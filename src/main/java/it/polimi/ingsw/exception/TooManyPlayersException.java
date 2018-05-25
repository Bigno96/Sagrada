package it.polimi.ingsw.exception;

public class TooManyPlayersException extends Exception {
    public TooManyPlayersException() { super(); }
    public TooManyPlayersException(String message) { super(message); }
    public TooManyPlayersException(String message, Throwable cause) { super(message, cause); }
    public TooManyPlayersException(Throwable cause) { super(cause); }
}
