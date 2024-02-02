package pt.ua.deti.ies.ReadEase.Controller;

import pt.ua.deti.ies.ReadEase.Service.AuthService;
import pt.ua.deti.ies.ReadEase.Service.UserService;
import pt.ua.deti.ies.ReadEase.dtos.SignupRequest;
import pt.ua.deti.ies.ReadEase.dtos.UserDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class SignupController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService UserService;

    @PostMapping("/register")
    public ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest) {
        UserDTO createdUser=authService.createUser(signupRequest);
        if(createdUser==null){
            return new ResponseEntity<>("User is not created, try again later",HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(createdUser,HttpStatus.OK);
        
        
    }

    @GetMapping("/confirm-email")
    public String confirmEmail(@RequestParam("id") int userId) {
        UserService.confirmEmail(userId);
        return "redirect:/login"; 
    }

}
