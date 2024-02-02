package pt.ua.deti.ies.ReadEase.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pt.ua.deti.ies.ReadEase.model.Notification;
import pt.ua.deti.ies.ReadEase.model.Users;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUser(Users user);

    List<Notification> findTop5ByUserOrderByIdDesc(Users user);
}