package exception;

public class WrongPositionException extends Exception{
    public WrongPositionException(){
        super();
    }
    public WrongPositionException(String message){
        super(message);
    }
}
