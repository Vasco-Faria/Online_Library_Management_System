package pt.ua.deti.ies.ReadEase.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pt.ua.deti.ies.ReadEase.Service.NotificationService;
import pt.ua.deti.ies.ReadEase.model.Notification;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationService bookAvailableNotificationService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Notification>> getUserNotifications(@PathVariable Integer userId) {
        try {
            List<Notification> notifications = bookAvailableNotificationService.getNotificationsByUserId(userId);
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Notification>> getAllNotifications() {
        try {
            List<Notification> notifications = bookAvailableNotificationService.getAllNotifications();
            if (notifications.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}