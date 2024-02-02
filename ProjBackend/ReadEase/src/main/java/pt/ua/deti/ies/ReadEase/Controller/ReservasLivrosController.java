package pt.ua.deti.ies.ReadEase.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pt.ua.deti.ies.ReadEase.Service.BookReservationService;
import pt.ua.deti.ies.ReadEase.dtos.BookReservationRequest;
import pt.ua.deti.ies.ReadEase.model.BookReserves;
import pt.ua.deti.ies.ReadEase.repository.BookReservesRepository;

import java.util.List;

import javax.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/BookReservations")
public class ReservasLivrosController {

    private final BookReservesRepository bookReservesRepository;

    @Autowired
    public ReservasLivrosController (BookReservesRepository bookReservesRepository) {
        this.bookReservesRepository = bookReservesRepository;
    }

    @Autowired
    private BookReservationService bookReservationService;

    @GetMapping("/all")
    public List<BookReserves> getAllBookReserves() {
        return bookReservesRepository.findAll();
    }
   

    @PostMapping("/reserve")
    public ResponseEntity<String> reserveBook(@RequestBody BookReservationRequest request) {
        try {
            bookReservationService.reserveBook(request);
            return ResponseEntity.ok("Reserved Sucessfully! You have 2 days to pick up the book");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error marking reservation");
        }
    }


    @PutMapping("/mark-as-with-user/{reservationId}")
    public ResponseEntity<String> markAsWithUser(@PathVariable int reservationId) {
        try {
            bookReservationService.markReservationAsWithUser(reservationId);
            
            return ResponseEntity.ok("Reservation marked as 'With User' successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error marking reservation as 'With User'");
        }
    }


    @PutMapping("/mark-as-collected/{reservationId}")
    public ResponseEntity<String> markAsCollected(@PathVariable int reservationId) {
        try {
            bookReservationService.markReservationAsCollected(reservationId);
            
            return ResponseEntity.ok("Reservation marked as 'Collected' successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error marking reservation as 'With User'");
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getReservationsByUserId(@PathVariable Integer userId) {
        try {
            List<BookReserves> reservations = bookReservationService.getReservationsByUserId(userId);
            if (reservations != null && !reservations.isEmpty()) {
                return ResponseEntity.ok(reservations);
            } else {
                return ResponseEntity.noContent().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving reservations for user");
        }
    }


    @PostMapping("/{reservationId}/request-extension")
    public ResponseEntity<?> requestExtension(@PathVariable Integer reservationId) {
        try {
            bookReservationService.requestExtension(reservationId);
            return ResponseEntity.ok("Pedido de extensão realizado com sucesso.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reserva não encontrada.");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao processar o pedido de extensão.");
        }
    }

     @PostMapping("/{alertId}/respond")
    public ResponseEntity<?> respondToExtensionRequest(@PathVariable Long alertId, @RequestParam boolean approve) {
        try {
            bookReservationService.respondToExtensionRequest(alertId, approve);
            return ResponseEntity.ok("Resposta ao pedido de extensão processada com sucesso.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Alerta de extensão não encontrado.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao responder ao pedido de extensão.");
        }
    }



    
}