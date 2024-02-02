package pt.ua.deti.ies.ReadEase.Service.Impl;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import pt.ua.deti.ies.ReadEase.Service.AuthService;
import pt.ua.deti.ies.ReadEase.dtos.SignupRequest;
import pt.ua.deti.ies.ReadEase.dtos.UserDTO;
import pt.ua.deti.ies.ReadEase.model.Users;
import pt.ua.deti.ies.ReadEase.repository.UsersRepository;
import pt.ua.deti.ies.ReadEase.Service.EmailService;

@Service
public class AuthServiceImpl implements AuthService{


    @Autowired
    private UsersRepository usersRepository;

    @Autowired 
    private EmailService EmailService;

    @Override
    public UserDTO createUser(SignupRequest signupRequest) {
    try {
        if (signupRequest.getNome() == null || signupRequest.getEmail() == null || signupRequest.getSenha() == null) {
            throw new IllegalArgumentException("Todos os campos devem ser preenchidos");
        }

        if (usersRepository.findByEmail(signupRequest.getEmail()) != null) {
            throw new IllegalArgumentException("O email já está em uso");
        }

        Users lastUser = usersRepository.findTopByOrderByIdDesc();

        Users user = new Users();
        user.setId(lastUser != null ? lastUser.getId() + 1 : 1);
        user.setNome(signupRequest.getNome());
        user.setEmail(signupRequest.getEmail());
        user.setSenha(new BCryptPasswordEncoder().encode(signupRequest.getSenha()));
        user.setTelemovel(signupRequest.getTelemovel());
        user.setVerified(true);
        
        if (signupRequest.getData_de_nascimento() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate dataNascimento = LocalDate.parse(signupRequest.getData_de_nascimento(), formatter);
            user.setData_de_nascimento(dataNascimento);
        }

        user.setTipo("normaluser");

        Users createdUser = usersRepository.save(user);

        UserDTO userDTO = new UserDTO();
        userDTO.setId(createdUser.getId());
        userDTO.setNome(createdUser.getNome());
        userDTO.setEmail(createdUser.getEmail());
        userDTO.setTelemovel(createdUser.getTelemovel());
        userDTO.setData_de_nascimento(createdUser.getData_de_nascimento());
        userDTO.setTipo(createdUser.getTipo());
        userDTO.setVerified(createdUser.getVerified());

        userDTO.setVerified(true);

        return userDTO;
    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
}


    
}
