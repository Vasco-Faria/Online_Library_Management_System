package pt.ua.deti.ies.ReadEase.repository;



import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.ua.deti.ies.ReadEase.model.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
   Optional<Room> findByNumber(int number);
}