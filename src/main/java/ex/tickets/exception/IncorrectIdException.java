package ex.tickets.exception;

public class IncorrectIdException extends Exception {
    public IncorrectIdException(String errorMessage, Long id) {
        super(errorMessage + id.toString());
    }
}
