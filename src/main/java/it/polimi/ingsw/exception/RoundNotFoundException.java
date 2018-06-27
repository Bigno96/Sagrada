package it.polimi.ingsw.exception;

public class RoundNotFoundException extends Exception {
        public RoundNotFoundException() { super(); }
        public RoundNotFoundException(String message) { super(message); }
        public RoundNotFoundException(String message, Throwable cause) { super(message, cause); }
        public RoundNotFoundException(Throwable cause) { super(cause); }

    }
