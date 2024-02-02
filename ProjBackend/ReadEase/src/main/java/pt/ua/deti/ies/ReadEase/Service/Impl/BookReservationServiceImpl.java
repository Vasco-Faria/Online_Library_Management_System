package pt.ua.deti.ies.ReadEase.Service.Impl;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import pt.ua.deti.ies.ReadEase.Service.NotificationService;
import pt.ua.deti.ies.ReadEase.Service.BookReservationService;
import pt.ua.deti.ies.ReadEase.Service.UserService;
import pt.ua.deti.ies.ReadEase.dtos.BookReservationRequest;
import pt.ua.deti.ies.ReadEase.model.Notification;
import pt.ua.deti.ies.ReadEase.model.BookReserves;
import pt.ua.deti.ies.ReadEase.model.ExtensionRequestAlert;
import pt.ua.deti.ies.ReadEase.model.Favoritos;
import pt.ua.deti.ies.ReadEase.model.Users;
import pt.ua.deti.ies.ReadEase.model.Status.ReservationBooksStatus;
import pt.ua.deti.ies.ReadEase.repository.BookReservesRepository;
import pt.ua.deti.ies.ReadEase.repository.ExtensionRequestAlertRepository;
import pt.ua.deti.ies.ReadEase.repository.FavoritosRepository;
import pt.ua.deti.ies.ReadEase.repository.UsersRepository;


@Service
public class BookReservationServiceImpl implements BookReservationService {

  
    @Autowired
    private BookReservesRepository bookReservesRepository;

    @Autowired
    private NotificationService notificationService;


    @Autowired
    private UserService userService;

    @Autowired
    private UsersRepository userRepository;

    @Autowired
    private FavoritosRepository favoritosRepository;

    @Autowired
    private ExtensionRequestAlertRepository extensionRequestAlertRepository;

    @Override
    public void reserveBook(BookReservationRequest request) {
        Users user = userService.findUserById(request.getUserId());

       
        boolean hasActiveReservations = bookReservesRepository
            .findAllByUserAndStatusNot(user, ReservationBooksStatus.COLLECTED)
            .stream()
            .anyMatch(reservation -> reservation.getStatus() != ReservationBooksStatus.COLLECTED);

        if (hasActiveReservations) {
            
            throw new IllegalStateException("Usuário já tem uma reserva ativa de livro.");
        }

        BookReserves reservation = new BookReserves();
        reservation.setBookId(request.getBookId());
        reservation.setUser(user);
        reservation.setStatus(ReservationBooksStatus.RESERVED);
        reservation.setStartTime(LocalDateTime.now());
        reservation.setEndTime(LocalDateTime.now().plusDays(2));

        bookReservesRepository.save(reservation);
    }

    public boolean checkBookAvailability(String bookId) {
       
        List<BookReserves> activeReservations = bookReservesRepository.findByBookIdAndStatusIn(
                bookId, Arrays.asList(ReservationBooksStatus.WITH_USER, ReservationBooksStatus.RESERVED)
        );
        
        return activeReservations.isEmpty();
    }

    @Override
    public void markReservationAsWithUser(int reservationId) {
        Optional<BookReserves> optionalReservation = bookReservesRepository.findById(reservationId);
        if (optionalReservation.isPresent()) {
            BookReserves reservation = optionalReservation.get();
    
           
            if (ReservationBooksStatus.RESERVED.equals(reservation.getStatus())) {
    
               
                if (isWithinTimeLimit(reservation.getStartTime())) {
                    
                    reservation.setStatus(ReservationBooksStatus.WITH_USER);
    
                    
                    reservation.setEndTime(LocalDateTime.now().plusDays(15));
    
                  
                    bookReservesRepository.save(reservation);
                } else {
                    throw new RuntimeException("Time limit exceeded for picking up the book");
                }
            } else {
                throw new RuntimeException("Invalid reservation status for marking as 'With User'");
            }
        } else {
            throw new RuntimeException("Reservation not found");
        }
    }

    private boolean isWithinTimeLimit(LocalDateTime creationTime) {
        
        LocalDateTime timeLimit = creationTime.plusDays(2);
    
        
        return LocalDateTime.now().isBefore(timeLimit);
    }

    @Scheduled(cron = "0 0 */6 * * *" ) 
    public void checkExpiredReservations() {
        List<BookReserves> expiredReservations = bookReservesRepository.findExpiredReservations(LocalDateTime.now());

        for (BookReserves reservation : expiredReservations) {
            reservation.setStatus(ReservationBooksStatus.COLLECTED);
            bookReservesRepository.save(reservation);
        }
    }

    @Override
    public List<BookReserves> getAllReservations() {
        return bookReservesRepository.findAll();
    }

    @Override
    public void markReservationAsCollected(int reservationId) {
        Optional<BookReserves> optionalReservation = bookReservesRepository.findById(reservationId);
        if (optionalReservation.isPresent()) {
            BookReserves reservation = optionalReservation.get();
    
           
            if (ReservationBooksStatus.WITH_USER.equals(reservation.getStatus())) {
    
               
                if (isWithinTimeLimit(reservation.getStartTime())) {
                    
                    reservation.setStatus(ReservationBooksStatus.COLLECTED);

                    String bookId=reservation.getBookId();
    
                  
                    bookReservesRepository.save(reservation);
                    notifyUsersForAvailableBook(bookId);
                } else {
                    throw new RuntimeException("Time limit exceeded for picking up the book");
                }
            } else {
                throw new RuntimeException("Invalid reservation status for marking as 'With User'");
            }
        } else {
            throw new RuntimeException("Reservation not found");
        }
    }

    @Override
    public List<BookReserves> getReservationsByUserId(Integer userId) {
        Users user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return Collections.emptyList();
        }

        List<BookReserves> allReservations = bookReservesRepository.findByUser(user);
        return allReservations.stream()
                            .filter(reservation -> !reservation.getStatus().equals(ReservationBooksStatus.COLLECTED))
                            .collect(Collectors.toList());
    }

    @Override
    public void requestExtension(Integer reservationId) {
        BookReserves reservation = bookReservesRepository.findById(reservationId)
                .orElseThrow(() -> new EntityNotFoundException("Reserva não encontrada"));

        if (reservation.isExtensionRequested()) {
            throw new IllegalStateException("Já existe um pedido de extensão para esta reserva.");
        }

        if (extensionRequestAlertRepository.existsByReservation(reservation)) {
            throw new IllegalStateException("Já existe um alerta de pedido de extensão para esta reserva.");
        }

        if (!reservation.getStatus().equals("WITH_USER") || 
            ChronoUnit.DAYS.between(LocalDateTime.now(), reservation.getEndTime()) >= 3) {
            throw new IllegalStateException("A extensão só pode ser solicitada se a reserva estiver com o usuário e faltarem menos de 3 dias para o término.");
        }

        reservation.setExtensionRequested(true);
        bookReservesRepository.save(reservation);

        ExtensionRequestAlert alert = new ExtensionRequestAlert(reservation, LocalDateTime.now(), ExtensionRequestAlert.Status.REQUESTED);
        extensionRequestAlertRepository.save(alert);
    }

    @Override
    public void respondToExtensionRequest(Long alertId, boolean approve) {
        ExtensionRequestAlert alert = extensionRequestAlertRepository.findById(alertId)
                .orElseThrow(() -> new EntityNotFoundException("Alerta de pedido de extensão não encontrado"));

        if (approve) {
            alert.setStatus(ExtensionRequestAlert.Status.APPROVED);

            BookReserves reservation = alert.getReservation();
            reservation.setEndTime(reservation.getEndTime().plusDays(7));
            bookReservesRepository.save(reservation);
        } else {
            alert.setStatus(ExtensionRequestAlert.Status.REJECTED);
        }

        extensionRequestAlertRepository.save(alert);
    }


    private void notifyUsersForAvailableBook(String bookId) {
        List<Favoritos> userFavorites = favoritosRepository.findByBookId(bookId);
        for (Favoritos favorite : userFavorites) {
            Users user = favorite.getUser();
            notificationService.createNotification(user,bookId, "The book you marked as favorite is now available.");
        }
    }
    



}
    
