package pt.ua.deti.ies.ReadEase.Service;

import java.io.IOException;
import java.util.List;

import pt.ua.deti.ies.ReadEase.dtos.EbookReservationDTO;
import pt.ua.deti.ies.ReadEase.model.EbookReserves;

public interface EbookReservationService {
    
     void reserveEbook(EbookReservationDTO request);

    boolean hasReservedEbook(int userId);

    void updateEbookReservationStatusToFinished(int userId);

    List<EbookReserves> getReservationsByUserId(Integer userId);

    public byte[] getRandomEpubBytes() throws IOException;
}
