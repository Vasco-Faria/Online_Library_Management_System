package pt.ua.deti.ies.ReadEase.model;


import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import pt.ua.deti.ies.ReadEase.model.Status.ReservationBooksStatus;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "book_reservations")
public class BookReserves implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reservationId;

    private String bookId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @Enumerated(EnumType.STRING)
    private ReservationBooksStatus status;
    
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private boolean extensionRequested = false;

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

   
    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public ReservationBooksStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationBooksStatus status) {
        this.status = status;
    }

 
    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public boolean isExtensionRequested() {
        return extensionRequested;
    }   

    public void setExtensionRequested(boolean extensionRequested) {
        this.extensionRequested = extensionRequested;
    }

    public boolean isExpiringSoon() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneDayFromNow = now.plusDays(1);
        return endTime.isAfter(now) && endTime.isBefore(oneDayFromNow);
    }

    public boolean isExpired() {
        LocalDateTime now = LocalDateTime.now();
        return endTime.isBefore(now);
    }
}
    