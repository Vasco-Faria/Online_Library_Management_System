package pt.ua.deti.ies.ReadEase.model;

import java.time.LocalDateTime;

public class Alert {

    private int reservationId;
    private LocalDateTime alertTime;
    private String alertType;

    public Alert(int reservationId, LocalDateTime alertTime, String alertType) {
        this.reservationId = reservationId;
        this.alertTime = alertTime;
        this.alertType = alertType;
    }

    public int getReservationId() {
        return reservationId;
    }

    public LocalDateTime getAlertTime() {
        return alertTime;
    }

    public String getAlertType() {
        return alertType;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public void setAlertTime(LocalDateTime alertTime) {
        this.alertTime = alertTime;
    }

    public void setAlertType(String alertType) {
        this.alertType = alertType;
    }

}