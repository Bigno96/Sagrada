package it.polimi.ingsw.exception;

public class WrongDiceSelectionException extends Exception {
    public WrongDiceSelectionException() { super(); }
    public WrongDiceSelectionException(String message) { super(message); }
    public WrongDiceSelectionException(String message, Throwable cause) { super(message, cause); }
    public WrongDiceSelectionException(Throwable cause) { super(cause); }
}
