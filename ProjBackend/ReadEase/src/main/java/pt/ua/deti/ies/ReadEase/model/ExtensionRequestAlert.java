package pt.ua.deti.ies.ReadEase.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class ExtensionRequestAlert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private BookReserves reservation;

    private LocalDateTime requestDate;

    private Status status;

    public ExtensionRequestAlert() {
    }

    public ExtensionRequestAlert(BookReserves reservation, LocalDateTime requestDate, Status status) {
        this.reservation = reservation;
        this.requestDate = requestDate;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BookReserves getReservation() {
        return reservation;
    }

    public void setReservation(BookReserves reservation) {
        this.reservation = reservation;
    }

    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDateTime requestDate) {
        this.requestDate = requestDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public enum Status {
        APPROVED,
        REQUESTED,
        REJECTED
    }
}



