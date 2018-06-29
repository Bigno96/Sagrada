package it.polimi.ingsw.exception;

public class AlreadyDoneException extends Exception{
    public AlreadyDoneException() { super(); }
    public AlreadyDoneException(String message) { super(message); }
    public AlreadyDoneException(String message, Throwable cause) { super(message, cause); }
    public AlreadyDoneException(Throwable cause) { super(cause); }
}

