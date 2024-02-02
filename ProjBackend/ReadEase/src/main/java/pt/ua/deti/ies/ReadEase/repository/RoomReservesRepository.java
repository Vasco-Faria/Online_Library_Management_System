package pt.ua.deti.ies.ReadEase.repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pt.ua.deti.ies.ReadEase.model.RoomReserves;
import pt.ua.deti.ies.ReadEase.model.Users;

import org.springframework.data.repository.query.Param;


@Repository
public interface RoomReservesRepository extends JpaRepository<RoomReserves,Long> {
    RoomReserves findByReservationid(@Param("reservationid")int reservationid);
    List<RoomReserves> findByStarttime(@Param("startTime") LocalDateTime startTime);
   
    @Query("SELECT rr FROM RoomReserves rr WHERE rr.room.number = :roomNumber")
    List<RoomReserves> findByRoomNumber(@Param("roomNumber") int roomNumber);


    @Query("SELECT rr FROM RoomReserves rr WHERE rr.user.nome = :userNome")
    List<RoomReserves> findByUserNome(@Param("userNome") String userName);

    @Query("SELECT MAX(rr.reservationid) FROM RoomReserves rr")
    Integer findLastReservationId();

    List<RoomReserves> findByStatus(@Param("status") String status);
    List<RoomReserves> findByUser(Users user);
    List<RoomReserves> findByUserAndStatusIn(Users user, Collection<String> statuses);
    int countByUserAndStatusNot(Users user, String string);

    
    @Query("SELECT r FROM RoomReserves r WHERE r.starttime >= :start AND r.endtime <= :end")
    List<RoomReserves> findReservationsBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
