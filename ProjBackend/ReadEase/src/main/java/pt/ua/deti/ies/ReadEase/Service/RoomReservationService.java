package pt.ua.deti.ies.ReadEase.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pt.ua.deti.ies.ReadEase.model.RoomReserves;
import pt.ua.deti.ies.ReadEase.Exceptions.MaxReservationsExceededException;
import pt.ua.deti.ies.ReadEase.model.Room;
import pt.ua.deti.ies.ReadEase.model.Users;
import pt.ua.deti.ies.ReadEase.repository.RoomRepository;
import pt.ua.deti.ies.ReadEase.repository.UsersRepository;

import pt.ua.deti.ies.ReadEase.repository.RoomReservesRepository;



@Service
public class RoomReservationService {

    @Autowired
    private RoomReservesRepository roomReservesRepository;

    @Autowired
    private RoomRepository salasRepository;

    @Autowired
    private UsersRepository usersRepository;

    public String criarReserva(Integer roomNumber, Integer userId, LocalDateTime startTime, LocalDateTime endTime) {
            Room room = salasRepository.findByNumber(roomNumber).orElseThrow(() -> new RuntimeException("Sala não encontrada"));

            
            Users user = usersRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            
            Integer lastReservationId = roomReservesRepository.findLastReservationId();
            int nonClosedReservationsCount = roomReservesRepository.countByUserAndStatusNot(user, "closed");

             if (nonClosedReservationsCount >= 2) {
                throw new MaxReservationsExceededException("Usuário já possui 2 reservas em aberto.");
             }

           
            Integer newReservationId = (lastReservationId != null) ? lastReservationId + 1 : 1;

            RoomReserves reserva = new RoomReserves();
            reserva.setReservationid(newReservationId);
            reserva.setRoom(room);
            reserva.setUser(user);
            reserva.setStarttime(startTime);
            reserva.setEndtime(endTime);
            reserva.setStatus("reserved");

            roomReservesRepository.save(reserva);

            return "Reserva criada com sucesso!";
    }

    public void addReservationNotes(int reservationId, String notes) {
        RoomReserves reservation = roomReservesRepository.findByReservationid(reservationId);
        if (reservation != null) {
            reservation.getNotes().add(notes);
            roomReservesRepository.save(reservation);
        }
    }

    public List<String> getReservationNotes(int reservationId) {
        RoomReserves reservation = roomReservesRepository.findByReservationid(reservationId);
        return (reservation != null) ? reservation.getNotes() : new ArrayList<>();
    }

    public void updateReservationStatus(int reservationId) {
        RoomReserves reservation = roomReservesRepository.findByReservationid(reservationId);

        if (reservation != null) {
            String currentStatus = reservation.getStatus();

            if ("reserved".equals(currentStatus)) {
                reservation.setStatus("ongoing");
            } else if ("ongoing".equals(currentStatus)) {
                reservation.setStatus("closed");
            }

            roomReservesRepository.save(reservation);
        }
    }

    public List<RoomReserves> getReservationsByStatus(String status) {
        return roomReservesRepository.findByStatus(status);
    }

    public List<RoomReserves> getReservationsByUserId(Integer userId) {
        Users user = usersRepository.findById(userId).orElse(null);
        if (user == null) {
            return Collections.emptyList();
        }
    
        List<String> desiredStatuses = Arrays.asList("reserved", "ongoing");
        return roomReservesRepository.findByUserAndStatusIn(user, desiredStatuses);
    }

    public String cancelReservation(Integer reservationId) {
        RoomReserves reservation = roomReservesRepository.findByReservationid(reservationId);
        if (reservation == null) {
            throw new RuntimeException("Reserva não encontrada");
        }
        reservation.setStatus("closed");
        roomReservesRepository.save(reservation);
        return "Reserva cancelada com sucesso";
    }

    public String extendReservationTime(Integer reservationId, int minutesToAdd) {
        RoomReserves reservation = roomReservesRepository.findByReservationid(reservationId);
        if (reservation != null) {
            if (!reservation.getIsExtraTimeAdded()) {
                LocalDateTime newEndTime = reservation.getEndtime().plusMinutes(minutesToAdd);
                reservation.setEndtime(newEndTime);
                reservation.setIsExtraTimeAdded(true);
                roomReservesRepository.save(reservation);
                return "Extra time added successfully";
            } else {
                return "Extra time has already been added to this reservation";
            }
        } else {
            throw new RuntimeException("Reservation not found");
        }
    }


    public List<RoomReserves> getTodayReservations() {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime startOfDay = today.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = today.toLocalDate().atTime(23, 59, 59);
        return roomReservesRepository.findReservationsBetween(startOfDay, endOfDay);
    }
    
    
    
    
}