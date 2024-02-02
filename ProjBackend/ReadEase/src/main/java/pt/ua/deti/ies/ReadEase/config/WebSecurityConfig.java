package pt.ua.deti.ies.ReadEase.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;




@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {
    
    @Bean 
    public PasswordEncoder PassowordEncoder(){return new BCryptPasswordEncoder();};


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity,CorsFilter corsFilter) throws Exception {
        return httpSecurity.addFilterBefore(corsFilter, CorsFilter.class).csrf().disable().authorizeHttpRequests().antMatchers("/**").permitAll().and().authorizeHttpRequests().antMatchers("/api/**").authenticated().and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().build();
    }
        

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
