package pt.ua.deti.ies.ReadEase.dtos;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ReservationRequest {
    private Integer roomNumber;
    private Integer userId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;


    public ReservationRequest() {
    }

    public void setRoomNumber(Integer roomNumber){
        this.roomNumber = roomNumber;
    }
    public Integer getRoomNumber(){
        return roomNumber;
    }

    public void setUserId(Integer userId){
        this.userId = userId;
    }

    public Integer getUserId(){
        return userId;
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

    


}