package pt.ua.deti.ies.ReadEase.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import pt.ua.deti.ies.ReadEase.model.Notification;

import pt.ua.deti.ies.ReadEase.model.Users;
import pt.ua.deti.ies.ReadEase.repository.NotificationRepository;
import pt.ua.deti.ies.ReadEase.repository.UsersRepository;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UsersRepository usersRepository;

   

    public void createNotification(Users user,String bookId, String message) {
       Notification notificacao = new Notification();
        notificacao.setUser(user);
        notificacao.setMessage(message);
        notificacao.setTimestamp(LocalDateTime.now());
        notificacao.setRead(false);
        notificacao.setBookId(bookId);

        notificationRepository.save(notificacao);
    }

     public List<Notification> getNotificationsByUserId(Integer userId) {
        Users user = usersRepository.findById(userId).orElse(null);
        System.out.println(user);
        if (user == null) {
            return Collections.emptyList();
        }
        
       
        List<Notification> notifications = notificationRepository.findTop5ByUserOrderByIdDesc(user);
        
        return notifications;
    }

    
    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }


   

}