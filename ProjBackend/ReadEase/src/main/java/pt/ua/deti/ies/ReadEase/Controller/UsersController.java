package pt.ua.deti.ies.ReadEase.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pt.ua.deti.ies.ReadEase.Service.UserService;
import pt.ua.deti.ies.ReadEase.dtos.UserDTO;
import pt.ua.deti.ies.ReadEase.model.BookReserves;
import pt.ua.deti.ies.ReadEase.model.EbookReserves;
import pt.ua.deti.ies.ReadEase.model.Users;
import pt.ua.deti.ies.ReadEase.repository.BookReservesRepository;
import pt.ua.deti.ies.ReadEase.repository.EbookReservesRepository;
import pt.ua.deti.ies.ReadEase.repository.UsersRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api/users")
public class UsersController {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private EbookReservesRepository ebookReservesRepository;

    @Autowired
    private BookReservesRepository bookReservesRepository;


    @GetMapping("/all")
    public List<Users> getAllUsers() {
        return usersRepository.findAll();
    }

    @GetMapping("/loggedinfo")
    public ResponseEntity<UserDTO> getUserByEmail(@RequestParam String email) {
        Users user = usersRepository.findFirstByEmail(email);
        if (user != null) {
            UserDTO userDto = new UserDTO();
            userDto.setId(user.getId());
            userDto.setNome(user.getNome());
            userDto.setTipo(user.getTipo());
            userDto.setVerified(user.getVerified());
    
            return ResponseEntity.ok(userDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/changemode/{userId}")
    public ResponseEntity<?> changeUserType(@PathVariable Integer userId) {
        try {
            userService.changeUserType(userId);
            return ResponseEntity.ok("Tipo de usuário alterado com sucesso");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao alterar o tipo do usuário");
        }
    }

    @GetMapping("/userReservations/{userId}/book/{bookId}")
    public ResponseEntity<?> getUserReservationsForBook(@PathVariable Integer userId, @PathVariable String bookId) {
        Optional<EbookReserves> ebookReservations = ebookReservesRepository.findByUserIdAndEbookId(userId, bookId);
        Optional<BookReserves> bookReservations = bookReservesRepository.findByUserIdAndBookId(userId, bookId);

        if (!ebookReservations.isPresent() && !bookReservations.isPresent()) {
            return ResponseEntity.notFound().build();
        } else {
            Map<String, Object> reservations = new HashMap<>();
            ebookReservations.ifPresent(reservation -> reservations.put("ebookReservation", reservation));
            bookReservations.ifPresent(reservation -> reservations.put("bookReservation", reservation));
            return ResponseEntity.ok(reservations);
        }
    }



    


}
