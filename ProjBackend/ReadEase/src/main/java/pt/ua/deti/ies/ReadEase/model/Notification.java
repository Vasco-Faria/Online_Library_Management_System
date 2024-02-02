package pt.ua.deti.ies.ReadEase.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "notification")
public class Notification {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id") 
    private Users user; 

    private String message;
    private LocalDateTime timestamp;
    private String bookId;
    private boolean isread;

    public Notification() {
    }

    public Notification(Users user, String message) {
        this.user = user;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.isread = false;
    }

    public Notification(Users user, String message, LocalDateTime timestamp,String bookId, boolean read) {
        this.user = user;
        this.message = message;
        this.timestamp = timestamp;
        this.bookId = bookId;
        this.isread = read;
    }

    public Long getId() {
        return id;
    }

    public Users getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public boolean isRead() {
        return isread;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setRead(boolean read) {
        this.isread = read;
    }


}