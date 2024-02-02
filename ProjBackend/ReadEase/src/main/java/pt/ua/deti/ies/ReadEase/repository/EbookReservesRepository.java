package pt.ua.deti.ies.ReadEase.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import pt.ua.deti.ies.ReadEase.model.EbookReserves;
import pt.ua.deti.ies.ReadEase.model.Users;

@Repository
public interface EbookReservesRepository extends JpaRepository<EbookReserves, Integer> {
    Optional<EbookReserves> findByReservationId(int reservationId);

    boolean existsByUserId(int userId);

    List<EbookReserves> findByUserIdAndStatus(int userId, String string);

    List<EbookReserves> findByUser(Users user);
    List<EbookReserves> findByUserAndStatus(Users user, String status);

    Optional<EbookReserves> findByUserIdAndEbookId(Integer userId, String ebookId);
}
