package pt.ua.deti.ies.ReadEase.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pt.ua.deti.ies.ReadEase.Service.RoomService;
import pt.ua.deti.ies.ReadEase.model.Room;
import pt.ua.deti.ies.ReadEase.repository.RoomRepository;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private RoomRepository salasRepository;

    @GetMapping("/all")
    public List<Room> getAllRooms() {
        return salasRepository.findAll();
    }

    @GetMapping("/available")
    public ResponseEntity<List<Room>> getAvailableRooms(
        @RequestParam("start_time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
        @RequestParam("end_time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        List<Room> availableRooms = roomService.findAvailableRooms(startTime, endTime);
        return new ResponseEntity<>(availableRooms, HttpStatus.OK);
    }

    @GetMapping("/updateAvailability")
    public ResponseEntity<String> updateRoomAvailability(@RequestParam int roomNumber) {
        roomService.updateRoomAvailability(roomNumber);
        return new ResponseEntity<>("Room availability updated successfully.", HttpStatus.OK);
    }
}