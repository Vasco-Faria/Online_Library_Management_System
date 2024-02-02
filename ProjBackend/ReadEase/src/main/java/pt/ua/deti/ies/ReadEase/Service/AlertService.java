package pt.ua.deti.ies.ReadEase.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import pt.ua.deti.ies.ReadEase.model.Alert;
import pt.ua.deti.ies.ReadEase.model.BookReserves;

@Service 
public class AlertService {


    
    @Autowired
    private BookReservationService bookReservationService;

    private List<Alert> currentDayAlerts = new ArrayList<>();

    @Scheduled(fixedRate = 2000)
    public void generateAndStoreAlerts(){
        try {
            cleanPreviousDayAlerts();
            List<BookReserves> allReservations = bookReservationService.getAllReservations();
            System.out.println("x");
            for (BookReserves reservation : allReservations) {
                if (reservation.isExpiringSoon()) {
                    currentDayAlerts.add(new Alert(reservation.getReservationId(), LocalDateTime.now(), "Expiring Soon"));
                } else if (reservation.isExpired()) {
                    currentDayAlerts.add(new Alert(reservation.getReservationId(), LocalDateTime.now(), "Expired"));
                }
            }
            System.out.println(currentDayAlerts);
        } catch (Exception e) {
            
        }
    }

    public List<Alert> getAlertsForCurrentDay() {
        return currentDayAlerts;
    }

  
    public void cleanPreviousDayAlerts() {
        currentDayAlerts.clear();
    }
}
