package pt.ua.deti.ies.ReadEase.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pt.ua.deti.ies.ReadEase.model.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {
    Optional<Users> findById(Integer userId);
    Users findTopByOrderByIdDesc();
    Users findFirstByEmail(String email);
    Users findByEmail(String email);
}