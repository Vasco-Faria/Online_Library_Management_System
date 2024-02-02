package pt.ua.deti.ies.ReadEase.MessageBroker;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public Queue reservationsQueue() {
        return new Queue("reservations");
    }

    @Bean
    public Queue UsersQueue(){
        return new Queue("users");
    }

    @Bean
    public Queue RoomsQueue(){
        return new Queue("rooms");
    }

}

