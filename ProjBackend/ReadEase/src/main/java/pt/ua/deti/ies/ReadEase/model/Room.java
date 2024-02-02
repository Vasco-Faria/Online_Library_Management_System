package pt.ua.deti.ies.ReadEase.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Salas")
public class Room {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int number;
    
    private Integer capacidade;
    
    private Boolean disponibilidade;

    @OneToMany(mappedBy = "room")
    private List<RoomReserves> reservations;


    public Room() {
    }

    public Room(int number, Integer capacidade, Boolean disponibilidade) {
        this.number = number;
        this.capacidade = capacidade;
        this.disponibilidade = disponibilidade;
    }


    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Integer getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(Integer capacidade) {
        this.capacidade = capacidade;
    }

    public Boolean getDisponibilidade() {
        return disponibilidade;
    }

    public void setDisponibilidade(Boolean disponibilidade) {
        this.disponibilidade = disponibilidade;
    }

    public Users orElseThrow(Object object) {
        return null;
    }

    public Room orElse(Object object) {
        return null;
    }

    public boolean isAvailable(LocalDateTime startTime, LocalDateTime endTime) {
        for (RoomReserves reservation : reservations) {
            if (!(endTime.isBefore(reservation.getStarttime()) || startTime.isAfter(reservation.getEndtime()))) {
                return false;
            }
        }
        return true;
    }
}