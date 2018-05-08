package exception;

public class WrongPositionException extends Exception{

    public WrongPositionException(){
        super();
    }
    public WrongPositionException(String message){
        super(message);
    }
    public WrongPositionException(String message, Throwable cause) { super(message, cause); }
    public WrongPositionException(Throwable cause) { super(cause); }
}
