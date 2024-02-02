package pt.ua.deti.ies.ReadEase.Service.jwt;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import  org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import pt.ua.deti.ies.ReadEase.model.Users;
import pt.ua.deti.ies.ReadEase.repository.UsersRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users user = usersRepository.findFirstByEmail(email);
        if (user == null){
            throw new UsernameNotFoundException("User not found",null);
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(),user.getSenha(),new ArrayList<>());
    }
    

    
}
