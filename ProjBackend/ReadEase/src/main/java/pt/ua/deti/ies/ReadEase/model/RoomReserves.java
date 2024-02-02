package pt.ua.deti.ies.ReadEase.model;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "room_reserves")
public class RoomReserves implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reservationid;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;  

    @ManyToOne
    @JoinColumn(name = "user_id") 
    private Users user;
    
    @ElementCollection
    @CollectionTable(name = "reservation_notes", joinColumns = @JoinColumn(name = "reservation_id"))
    @Column(name = "note")
    private List<String> notes;

    private String status;
    private LocalDateTime starttime;
    private LocalDateTime endtime;
    private boolean isExtraTimeAdded = false;

    public RoomReserves() {
    }

    public RoomReserves(int reservationid, Room room, Users user, String status, LocalDateTime start_time, LocalDateTime end_time) {
        this.reservationid = reservationid;
        this.room = room;
        this.user = user;
        this.status = status;
        this.starttime = start_time;
        this.endtime = end_time;
    }

    public int getReservationid() {
        return reservationid;
    }

    public void setReservationid(int reservationid) {
        this.reservationid = reservationid;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room2) {
        this.room = room2;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public boolean getIsExtraTimeAdded() {
        return isExtraTimeAdded;
    }

    public void setIsExtraTimeAdded(boolean isExtraTimeAdded) {
        this.isExtraTimeAdded = isExtraTimeAdded;
    }

    public String getStatus() {
        return status;
    }
    public List<String> getNotes(){
        return notes;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getStarttime() {
        return starttime;
    }
    
    public void setStarttime(LocalDateTime starttime) {
        this.starttime = starttime;
    }

    public LocalDateTime getEndtime() {
        return endtime;
    }

    public void setEndtime(LocalDateTime end_time) {
        this.endtime = end_time;
    }

    public void setNotes(List<String> notes2) {
    }

    public boolean isPresent() {
        return false;
    }
   

    
}
