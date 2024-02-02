package pt.ua.deti.ies.ReadEase.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pt.ua.deti.ies.ReadEase.model.Room;
import pt.ua.deti.ies.ReadEase.repository.RoomRepository;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;



    public List<Room> findAvailableRooms(LocalDateTime startTime, LocalDateTime endTime) {
        List<Room> allRooms = roomRepository.findAll(); 
        List<Room> availableRooms = new ArrayList<>();

        for (Room room : allRooms) {
            if (room.isAvailable(startTime, endTime)) {
                availableRooms.add(room);
            }
        }

        return availableRooms;
    }



    public void updateRoomAvailability(int RoomNum) {
        List<Room> allRooms =roomRepository.findAll();
        for(Room sala:allRooms){
            if (sala.getNumber()==RoomNum){
                sala.setDisponibilidade(!sala.getDisponibilidade());
                break;
            }
        }

    }
}
