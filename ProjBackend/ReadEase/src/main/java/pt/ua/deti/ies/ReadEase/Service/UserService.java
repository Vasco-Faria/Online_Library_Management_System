package pt.ua.deti.ies.ReadEase.Service;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pt.ua.deti.ies.ReadEase.model.Users;
import pt.ua.deti.ies.ReadEase.repository.UsersRepository;

@Service
public class UserService {
    @Autowired
    private UsersRepository userRepository;


    public void confirmEmail(int userId) {
        Optional<Users> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            Users user = optionalUser.get();
            user.setVerified(true);
            userRepository.save(user);
        } else {
            throw new EntityNotFoundException("Usuário não encontrado com o ID: " + userId);
        }
    }

    public Users findUserById(int userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public void changeUserType(Integer userId) {
        Optional<Users> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            Users user = optionalUser.get();
            if (user.getTipo().equals("bibliotecario")) {
                user.setTipo("normaluser");
            } else {
                user.setTipo("biliotecario");
            }
            userRepository.save(user);
        } else {
            throw new EntityNotFoundException("Usuário não encontrado com o ID: " + userId);
        }
    }

}
