package pt.ua.deti.ies.ReadEase.Exceptions;

public class MaxReservationsExceededException extends RuntimeException {
    public MaxReservationsExceededException(String message) {
        super(message);
    }
}