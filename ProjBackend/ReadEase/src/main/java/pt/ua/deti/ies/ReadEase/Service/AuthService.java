package pt.ua.deti.ies.ReadEase.Service;


import pt.ua.deti.ies.ReadEase.dtos.SignupRequest;
import pt.ua.deti.ies.ReadEase.dtos.UserDTO;

public interface AuthService {
    UserDTO createUser(SignupRequest signupRequest);
}
