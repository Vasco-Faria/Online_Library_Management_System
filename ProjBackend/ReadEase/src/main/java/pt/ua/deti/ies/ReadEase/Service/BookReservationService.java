package pt.ua.deti.ies.ReadEase.Service;

import java.util.List;

import pt.ua.deti.ies.ReadEase.dtos.BookReservationRequest;
import pt.ua.deti.ies.ReadEase.model.BookReserves;

public interface BookReservationService {
    void reserveBook(BookReservationRequest request);
    boolean checkBookAvailability(String bookId);
    void markReservationAsWithUser(int reservationId);
    List<BookReserves> getAllReservations();
    void markReservationAsCollected(int reservationId);
    List<BookReserves> getReservationsByUserId(Integer userId);
    void requestExtension(Integer reservationId);
    void respondToExtensionRequest(Long alertId, boolean approve);
}
