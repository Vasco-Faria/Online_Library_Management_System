package pt.ua.deti.ies.ReadEase.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthenticationResponse {
    
    private String jwt;
}
