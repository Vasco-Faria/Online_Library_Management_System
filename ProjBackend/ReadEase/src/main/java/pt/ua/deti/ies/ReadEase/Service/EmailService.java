package pt.ua.deti.ies.ReadEase.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import pt.ua.deti.ies.ReadEase.model.Users;

@Service
public class EmailService {

   @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SimpleMailMessage preConfiguredMessage;

    
    

    public void sendNewMail(Users user)
    {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Confirmação de E-mail");
        message.setText("Clique no link a seguir para confirmar seu e-mail: http://localhost:8080/confirm-email?id=" + user.getId());
        mailSender.send(message);
    }

    public void sendPreConfiguredMail(String message)
    {
        SimpleMailMessage mailMessage = new SimpleMailMessage(preConfiguredMessage);
        mailMessage.setText(message);
        mailSender.send(mailMessage);
    }
}


