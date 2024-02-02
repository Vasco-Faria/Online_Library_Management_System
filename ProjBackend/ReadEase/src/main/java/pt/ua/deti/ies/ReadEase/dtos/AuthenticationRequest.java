package pt.ua.deti.ies.ReadEase.dtos;

import lombok.Data;

@Data
public class AuthenticationRequest {
    
    private String email;
    private String senha;
}
