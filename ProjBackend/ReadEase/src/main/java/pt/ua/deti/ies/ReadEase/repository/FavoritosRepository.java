package pt.ua.deti.ies.ReadEase.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pt.ua.deti.ies.ReadEase.model.Favoritos;
import pt.ua.deti.ies.ReadEase.model.Users;

@Repository
public interface FavoritosRepository extends JpaRepository<Favoritos, Long> {
    List<Favoritos> findByUser(Users user);
    Optional<Favoritos> findByUserAndBookId(Users user, String bookId);
    List<Favoritos> findByBookId(String bookId);
}
