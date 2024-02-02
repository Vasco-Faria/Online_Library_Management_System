package pt.ua.deti.ies.ReadEase.dtos;

import lombok.Data;

@Data
public class BookReservationRequest {
   

    private String bookId;
    private int userId;
}
