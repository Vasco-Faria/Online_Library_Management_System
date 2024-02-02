package pt.ua.deti.ies.ReadEase.Service.Impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import pt.ua.deti.ies.ReadEase.Service.EbookReservationService;
import pt.ua.deti.ies.ReadEase.Service.UserService;
import pt.ua.deti.ies.ReadEase.dtos.EbookReservationDTO;
import pt.ua.deti.ies.ReadEase.model.EbookReserves;
import pt.ua.deti.ies.ReadEase.model.Users;
import pt.ua.deti.ies.ReadEase.repository.EbookReservesRepository;
import pt.ua.deti.ies.ReadEase.repository.UsersRepository;

@Service
public class EbookReservationServiceImpl implements EbookReservationService {

    @Autowired
    private EbookReservesRepository ebookReservesRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UsersRepository userRepository;

    @Autowired
    private final ResourceLoader resourceLoader;

    public EbookReservationServiceImpl(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }


    @Override
    public void reserveEbook(EbookReservationDTO request){
        EbookReserves ebookReserves = new EbookReserves();
        ebookReserves.setEbookId(request.getEbookId());

        System.out.println(request.getEbookId());

        
        Users user = userService.findUserById(request.getUserId());
        if (user != null) {
            ebookReserves.setUser(user);
        }

        ebookReserves.setStatus("reserved");
        ebookReserves.setStartTime(LocalDateTime.now());
        ebookReserves.setEndTime(LocalDateTime.now().plusDays(7));
        ebookReservesRepository.save(ebookReserves);
    }

    @Override
    public boolean hasReservedEbook(int userId) {
        List<EbookReserves> reservations = ebookReservesRepository.findByUserIdAndStatus(userId, "reserved");
        return !reservations.isEmpty();
    }

    @Override
    public void updateEbookReservationStatusToFinished(int userId) {
        List<EbookReserves> reservations = ebookReservesRepository.findByUserIdAndStatus(userId, "reserved");
        if (!reservations.isEmpty()) {
            EbookReserves reservation = reservations.get(0);
            reservation.setStatus("finished");
            ebookReservesRepository.save(reservation);
        }
    }

    @Override
    public List<EbookReserves> getReservationsByUserId(Integer userId) {
        Users user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return Collections.emptyList(); 
        }

        return ebookReservesRepository.findByUserAndStatus(user,"reserved");
    }

    private List<Path> listAllEpubFiles() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:/epubs");
        Path epubDirectory = resource.getFile().toPath();
        return Files.walk(epubDirectory)
                .filter(path -> path.toString().toLowerCase().endsWith(".epub"))
                .collect(Collectors.toList());
    }

    @Override
    public byte[] getRandomEpubBytes() throws IOException {
        List<Path> epubFiles = listAllEpubFiles();
        if (!epubFiles.isEmpty()) {
            Random random = new Random();
            Path randomEpubPath = epubFiles.get(random.nextInt(epubFiles.size()));
            return Files.readAllBytes(randomEpubPath);
        } else {
            throw new IOException("No EPUB files found.");
        }
    }
}