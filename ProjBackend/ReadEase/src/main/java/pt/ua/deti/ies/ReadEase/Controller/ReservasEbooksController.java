package pt.ua.deti.ies.ReadEase.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pt.ua.deti.ies.ReadEase.Service.EbookReservationService;
import pt.ua.deti.ies.ReadEase.dtos.EbookReservationDTO;
import pt.ua.deti.ies.ReadEase.model.EbookReserves;
import pt.ua.deti.ies.ReadEase.repository.EbookReservesRepository;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/EbookReservations")
public class ReservasEbooksController {

    @Autowired
    private EbookReservesRepository ebookReservesRepository;

    @Autowired
    private EbookReservationService ebookReservationService;


    @GetMapping("/all")
    public List<EbookReserves> getAllEbookReserves() {
        return ebookReservesRepository.findAll();
    }

    @PostMapping("/reserve")
    public ResponseEntity<?> reserveEbook(@RequestBody EbookReservationDTO request) {
        try {
           
            boolean hasReservedEbook = ebookReservationService.hasReservedEbook(request.getUserId());
            
            if (hasReservedEbook) {
               
                return ResponseEntity.status(HttpStatus.CONFLICT).body("User already has a reserved ebook. Do you want to change the reservation?");
            }

            
            ebookReservationService.reserveEbook(request);
            return ResponseEntity.ok("Ebook reserved successfully");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error reserving ebook");
        }
    }

    @PostMapping("/changeAndReserveEbook")
    public ResponseEntity<?> changeAndReserveEbook(@RequestBody EbookReservationDTO request) {
        try {
           
            boolean hasReservedEbook = ebookReservationService.hasReservedEbook(request.getUserId());

            if (hasReservedEbook) {
               
                ebookReservationService.updateEbookReservationStatusToFinished(request.getUserId());

               
                ebookReservationService.reserveEbook(request);
                return ResponseEntity.ok("Ebook reservation changed successfully");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No active ebook reservation found for user");
            }
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error changing ebook reservation");
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getEbookReservationsByUserId(@PathVariable Integer userId) {
        try {
            List<EbookReserves> reservations = ebookReservationService.getReservationsByUserId(userId);
            if (reservations != null && !reservations.isEmpty()) {
                return ResponseEntity.ok(reservations);
            } else {
                return ResponseEntity.noContent().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving ebook reservations for user");
        }
    }


    @GetMapping("/epubs")
    public ResponseEntity<?> downloadEpub() {
        try {
            byte[] epubBytes = ebookReservationService.getRandomEpubBytes();

            ByteArrayResource resource = new ByteArrayResource(epubBytes);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/epubs+zip"));
            headers.setContentDispositionFormData("attachment", "random.epub");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(epubBytes.length)
                    .body(resource);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error reading EPUB file");
        }
    }

    @PostMapping("/finish/{userId}")
    public ResponseEntity<?> finishEbookReservation(@PathVariable Integer userId) {
        try {
            boolean hasReservedEbook = ebookReservationService.hasReservedEbook(userId);

            if (hasReservedEbook) {
                ebookReservationService.updateEbookReservationStatusToFinished(userId);
                return ResponseEntity.ok("Ebook reservation marked as finished");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No active ebook reservation found for user");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error finishing ebook reservation");
        }
    }


}

