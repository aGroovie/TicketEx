package ex.tickets.exception;

public class NoTicketsToUpdateException extends Exception{
    public NoTicketsToUpdateException(String errorMessage){
        super(errorMessage);
    }
}
