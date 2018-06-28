package it.polimi.ingsw.exception;

public class WrongCellSelectionException extends Exception {
    public WrongCellSelectionException() { super(); }
    public WrongCellSelectionException(String message) { super(message); }
    public WrongCellSelectionException(String message, Throwable cause) { super(message, cause); }
    public WrongCellSelectionException(Throwable cause) { super(cause); }
}

