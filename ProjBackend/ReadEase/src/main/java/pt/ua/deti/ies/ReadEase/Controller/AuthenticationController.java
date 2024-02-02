package pt.ua.deti.ies.ReadEase.Controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pt.ua.deti.ies.ReadEase.Service.UserService;
import pt.ua.deti.ies.ReadEase.Service.jwt.UserDetailsServiceImpl;
import pt.ua.deti.ies.ReadEase.dtos.AuthenticationRequest;
import pt.ua.deti.ies.ReadEase.dtos.AuthenticationResponse;
import pt.ua.deti.ies.ReadEase.model.Users;
import pt.ua.deti.ies.ReadEase.repository.UsersRepository;
import pt.ua.deti.ies.ReadEase.utils.JwtUtil;


@RestController
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;


    @Autowired
    private UsersRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getSenha()));

            Users user = userRepository.findFirstByEmail(authenticationRequest.getEmail());
            if (user != null && !user.getVerified()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("A conta de e-mail ainda não foi verificada");
            }

            System.out.println(user);

           
            final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
            final String jwt = jwtUtil.generateToken(userDetails.getUsername());
            return ResponseEntity.ok(new AuthenticationResponse(jwt));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais incorretas");
        } catch (DisabledException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não criado. Registre o usuário primeiro");
        }
    }
       


   

}
    
     
