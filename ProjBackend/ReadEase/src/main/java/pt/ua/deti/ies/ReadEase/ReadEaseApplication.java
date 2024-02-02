package pt.ua.deti.ies.ReadEase;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableScheduling
@EnableWebMvc
@SpringBootApplication
public class ReadEaseApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReadEaseApplication.class, args);
	}


}
