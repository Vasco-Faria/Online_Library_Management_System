package pt.ua.deti.ies.ReadEase.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pt.ua.deti.ies.ReadEase.model.RoomReserves;

import pt.ua.deti.ies.ReadEase.repository.RoomReservesRepository;
import pt.ua.deti.ies.ReadEase.Exceptions.MaxReservationsExceededException;
import pt.ua.deti.ies.ReadEase.Service.RoomReservationService;
import pt.ua.deti.ies.ReadEase.dtos.ApiResponse;
import pt.ua.deti.ies.ReadEase.dtos.ReservationRequest;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/RoomReservations")
public class ReservasSalasController {

    @Autowired
    private RoomReservesRepository roomReservesRepository;

    @Autowired
    private RoomReservationService roomReservationService;



    @GetMapping("/all")
    public List<RoomReserves> getAllUsers() {
        return roomReservesRepository.findAll();
    }

    @GetMapping("/{reservationId}")
    public ResponseEntity<RoomReserves> getReservationById(@PathVariable int reservationId) {
        RoomReserves reservation = roomReservesRepository.findByReservationid(reservationId);

        if (reservation != null) {
            return new ResponseEntity<>(reservation, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/today")
    public ResponseEntity<List<RoomReserves>> getTodayReservations() {
        List<RoomReserves> reservations = roomReservationService.getTodayReservations();
        if (reservations != null && !reservations.isEmpty()) {
            return new ResponseEntity<>(reservations, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/reserve")
    public ResponseEntity<ApiResponse> newReserve(@RequestBody ReservationRequest request) {
        try {
            String message = roomReservationService.criarReserva(request.getRoomNumber(), request.getUserId(), request.getStartTime(), request.getEndTime());
            ApiResponse response = new ApiResponse(message);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (MaxReservationsExceededException e) {
            return handleMaxReservationsExceededException(e);
        } catch (RuntimeException e) {
            return handleRuntimeException(e);
        }
    }

    @ExceptionHandler(MaxReservationsExceededException.class)
    public ResponseEntity<ApiResponse> handleMaxReservationsExceededException(MaxReservationsExceededException ex) {
        ApiResponse response = new ApiResponse(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST); 
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse> handleRuntimeException(RuntimeException e) {
        ApiResponse response = new ApiResponse("Erro ao criar a reserva. Consulte os logs para mais detalhes.");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/{reservationId}/addNotes")
    public ResponseEntity<String> addReservationNotes(
            @PathVariable int reservationId,
            @RequestBody String notes) {
        
        roomReservationService.addReservationNotes(reservationId, notes);

        return new ResponseEntity<>("Notes added to reservation successfully.", HttpStatus.OK);
    }


    @GetMapping("/getNotes/{reservationId}")
    public ResponseEntity<List<String>> getReservationNotes(@PathVariable int reservationId) {
        List<String> notes = roomReservationService.getReservationNotes(reservationId);
        return new ResponseEntity<>(notes, HttpStatus.OK);
    }

    @PostMapping("/{reservationId}/updateStatus")
    public ResponseEntity<String> updateReservationStatus(@PathVariable int reservationId) {
        roomReservationService.updateReservationStatus(reservationId);
        return new ResponseEntity<>("Reservation status updated successfully.", HttpStatus.OK);
    }

    @GetMapping("/findByStatus")
    public ResponseEntity<List<RoomReserves>> getReservationsByStatus(@RequestParam String status) {
        List<RoomReserves> reservations = roomReservationService.getReservationsByStatus(status);

        if (reservations != null && !reservations.isEmpty()) {
            return new ResponseEntity<>(reservations, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/extendReservation/{reservationId}")
    public ResponseEntity<ApiResponse> extendReservationTime(@PathVariable Integer reservationId) {
        try {
            String message = roomReservationService.extendReservationTime(reservationId, 30); 
            ApiResponse response = new ApiResponse(message);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ApiResponse response = new ApiResponse(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }



    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getRoomReservationsByUserId(@PathVariable Integer userId) {
        try {
            List<RoomReserves> reservations = roomReservationService.getReservationsByUserId(userId);
            if (reservations != null && !reservations.isEmpty()) {
                return ResponseEntity.ok(reservations);
            } else {
                return ResponseEntity.noContent().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving room reservations for user");
        }
    }

  
    @PostMapping("/reserve/cancel/{reservationId}")
    public ResponseEntity<ApiResponse> cancelReservation(@PathVariable Integer reservationId) {
        try {
            String message = roomReservationService.cancelReservation(reservationId);
            ApiResponse response = new ApiResponse(message);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ApiResponse response = new ApiResponse(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }






}