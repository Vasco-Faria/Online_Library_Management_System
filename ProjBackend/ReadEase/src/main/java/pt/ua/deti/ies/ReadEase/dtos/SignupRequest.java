package pt.ua.deti.ies.ReadEase.dtos;


import org.springframework.lang.Nullable;

import lombok.Data;

@Data
public class SignupRequest {
    
    private String nome;
    private String email;
    private String senha;
    
    @Nullable
    private String telemovel;

    @Nullable
    private String data_de_nascimento;

}
