package pt.ua.deti.ies.ReadEase.MessageBroker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import pt.ua.deti.ies.ReadEase.Service.BookReservationService;
import pt.ua.deti.ies.ReadEase.model.BookReserves;
import pt.ua.deti.ies.ReadEase.model.EbookReserves;
import pt.ua.deti.ies.ReadEase.model.RoomReserves;
import pt.ua.deti.ies.ReadEase.repository.EbookReservesRepository;
import pt.ua.deti.ies.ReadEase.repository.RoomReservesRepository;
import pt.ua.deti.ies.ReadEase.model.Room;
import pt.ua.deti.ies.ReadEase.repository.RoomRepository;
import pt.ua.deti.ies.ReadEase.model.Users;
import pt.ua.deti.ies.ReadEase.model.Status.ReservationBooksStatus;
import pt.ua.deti.ies.ReadEase.repository.UsersRepository;
import pt.ua.deti.ies.ReadEase.repository.BookReservesRepository;



@Component
public class RabbitConsumer {

    @Autowired
    private RoomRepository salasRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private BookReservationService bookReservationService;

    @Autowired
    private RoomReservesRepository roomReservesRepository;

    @Autowired
    private EbookReservesRepository EbookReservesRepository;

    @Autowired
    private BookReservesRepository BookReservesRepository;

    @RabbitListener(queues = "reservations")
public void handleMessageReservations(String mensagem) {
    try {
        System.out.println("Reserva Recebida: " + mensagem);

        JSONObject json = new JSONObject(mensagem);

        int reservationId = json.getInt("reservation_id");

       
        RoomReserves roomReserve = roomReservesRepository.findByReservationid(reservationId);
        EbookReserves ebookReserve = EbookReservesRepository.findById(reservationId).orElse(null);
        BookReserves bookReserve = BookReservesRepository.findById(reservationId).orElse(null);
       
        if (roomReserve == null && ebookReserve == null && bookReserve == null) {
            String resourceType = json.getString("tipo");
            switch (resourceType) {
                case "ReservationRoom":
                    handleRoomReservation(json);
                    break;
                case "ReservationEbook":
                    handleEbookReservation(json);
                    break;
                case "ReservationBook":
                    handleBookReservation(json);
                    break;
                default:
                    System.out.println("Tipo de recurso desconhecido: " + resourceType);
                    break;
            }
        } else {
            System.out.println("Já existe uma reserva com o ID " + reservationId + ". A reserva não foi processada novamente.");
        }
    } catch (JSONException e) {
        System.err.println("Erro ao processar a mensagem JSON: " + e.getMessage());
    }
}
   
    
private void handleRoomReservation(JSONObject json) {
        int roomId = json.getInt("room");
        int userId = json.getInt("user");
        String status = json.getString("status");

        

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startTime = LocalDateTime.parse(json.getString("start_time"), formatter);
        LocalDateTime endTime = LocalDateTime.parse(json.getString("end_time"), formatter);

    
        Room room = salasRepository.findByNumber(roomId).orElse(null);

        Users user = usersRepository.findById(userId).orElse(null);

        System.out.println(roomId);
        System.out.println(userId);
        System.out.println(room);
        System.out.println(user);

        if (room != null && user != null) {
            RoomReserves reservation = new RoomReserves();
            reservation.setRoom(room);
            reservation.setUser(user);
            reservation.setStatus(status);
            reservation.setStarttime(startTime);
            reservation.setEndtime(endTime);
            reservation.setReservationid(json.getInt("reservation_id"));

            roomReservesRepository.save(reservation);
            System.out.println("Reserva salva com sucesso");
        } else {
            System.out.println("Sala ou usuário não encontrados no banco de dados.");
        }
    }
    
    private void handleEbookReservation(JSONObject json) {
        int userId = json.getInt("user");
        String status = json.getString("status");
    
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startTime = LocalDateTime.parse(json.getString("start_time"), formatter);
        LocalDateTime endTime = LocalDateTime.parse(json.getString("end_time"), formatter);
    
        String ebookId = json.getString("ebook_id");
    
        Users user = usersRepository.findById(userId).orElse(null);
    
        System.out.println(userId);
        System.out.println(user);
    
        if (user != null) {
            EbookReserves ebookReservation = new EbookReserves();
            ebookReservation.setEbookId(ebookId);
            ebookReservation.setUser(user);
            ebookReservation.setStatus(status);
            ebookReservation.setStartTime(startTime);
            ebookReservation.setEndTime(endTime);
            ebookReservation.setReservationId(json.getInt("reservation_id"));
    
            EbookReservesRepository.save(ebookReservation);
            System.out.println("Reserva de Ebook salva com sucesso");
        } else {
            System.out.println("Usuário não encontrado no banco de dados.");
        }
    }
       
    
    public void handleBookReservation(JSONObject json) {
        int userId = json.getInt("user");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startTime = LocalDateTime.parse(json.getString("start_time"), formatter);
        LocalDateTime endTime = LocalDateTime.parse(json.getString("end_time"), formatter);

        String bookId = json.getString("book_id");

        Users user = usersRepository.findById(userId).orElse(null);

        if (user != null) {
           
            boolean isBookAvailable = bookReservationService.checkBookAvailability(bookId);

            if (isBookAvailable) {
                BookReserves bookReservation = new BookReserves();
                bookReservation.setBookId(bookId);
                bookReservation.setUser(user);
                bookReservation.setStatus(ReservationBooksStatus.RESERVED);
                bookReservation.setStartTime(startTime);
                bookReservation.setEndTime(endTime);
                bookReservation.setReservationId(json.getInt("reservation_id"));

                BookReservesRepository.save(bookReservation);
                System.out.println("Reserva de Livro salva com sucesso");
            } else {
                System.out.println("O livro não está disponível para reserva.");
            }
        } else {
            System.out.println("Usuário não encontrado no banco de dados.");
        }
    }


    @RabbitListener(queues = "users")
    public void handleMessageUsers(String mensagem) {
    try {
        System.out.println("User Recebido: " + mensagem);
        JSONObject json = new JSONObject(mensagem);
        String tipo = json.getString("tipo");
        int userId = json.getInt("user_id");
        String name = json.getString("name");
        String email = json.getString("email");
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String senha =json.optString("senha", null);
        System.out.println(senha);
        if(senha!=null){
            senha = encoder.encode(senha);
        }else {
            senha= null;
        }
        String phoneNumber = json.getString("phonenumber");
        LocalDate birthdate = LocalDate.parse(json.getString("birthdate"));

        boolean isVerified = "bibliotecario".equals(tipo);


    
        Optional<Users> existingUser = usersRepository.findById(userId);
        if (existingUser.isEmpty()) {
            Users user = new Users(userId, name, email, senha, tipo, phoneNumber, birthdate,isVerified);
            usersRepository.save(user);
            System.out.println("Usuário salvo com sucesso!");
        } else {
            System.out.println("Usuário com ID " + userId + " já existe. Não foi salvo.");
        }
    } catch (JSONException e) {
        System.err.println("Erro ao processar a mensagem JSON: " + e.getMessage());
    }
}


    @RabbitListener(queues = "rooms")
    public void handleMessageRooms(String mensagem) {
        System.out.println("Room Recebida: " + mensagem);
        JSONObject json = new JSONObject(mensagem);
        int number = json.getInt("number");
        int capacity = json.getInt("capacity");

        Room sala=new Room(number,capacity,true);
        salasRepository.save(sala);

    }
}

