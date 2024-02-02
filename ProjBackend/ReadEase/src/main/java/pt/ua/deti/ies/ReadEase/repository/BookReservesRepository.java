package pt.ua.deti.ies.ReadEase.repository;


import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import pt.ua.deti.ies.ReadEase.model.BookReserves;

import pt.ua.deti.ies.ReadEase.model.Users;
import pt.ua.deti.ies.ReadEase.model.Status.ReservationBooksStatus;

@Repository
public interface BookReservesRepository extends JpaRepository<BookReserves, Integer> {
    Optional<BookReserves> findByReservationId(int reservationId);


    List<BookReserves> findByBookIdAndStatusIn(String bookId, List<ReservationBooksStatus> asList);

    @Query("SELECT r FROM BookReserves r WHERE r.status = 'Reserved' AND r.startTime < :currentTime")
    List<BookReserves> findExpiredReservations(@Param("currentTime") LocalDateTime currentTime);


    Collection<BookReserves> findAllByUserAndStatusNot(Users user, ReservationBooksStatus collected);



    List<BookReserves> findByUser(Users user);

    Optional<BookReserves> findByUserIdAndBookId(Integer userId, String bookId);

}

