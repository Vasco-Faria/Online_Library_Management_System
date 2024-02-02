package pt.ua.deti.ies.ReadEase.Service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pt.ua.deti.ies.ReadEase.dtos.ReviewDTO;
import pt.ua.deti.ies.ReadEase.model.Review;
import pt.ua.deti.ies.ReadEase.model.Users;
import pt.ua.deti.ies.ReadEase.repository.BookReservesRepository;
import pt.ua.deti.ies.ReadEase.repository.EbookReservesRepository;
import pt.ua.deti.ies.ReadEase.repository.ReviewRepository;
import pt.ua.deti.ies.ReadEase.repository.UsersRepository;


@Service
public class ReviewService {
   @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private EbookReservesRepository ebookReservesRepository;

    @Autowired
    private BookReservesRepository bookReservesRepository;
    

    public Review addReview(ReviewDTO reviewDTO) {
        
        boolean ebookReservationExists = ebookReservesRepository
            .findByUserIdAndEbookId(reviewDTO.getUserId(), reviewDTO.getBookId())
            .isPresent();

        boolean bookReservationExists = bookReservesRepository
            .findByUserIdAndBookId(reviewDTO.getUserId(), reviewDTO.getBookId())
            .isPresent();

        if (!ebookReservationExists && !bookReservationExists) {
            throw new RuntimeException("Reserva de eBook ou livro não encontrada");
        }
        
        boolean reviewExists = reviewRepository
            .existsByUserIdAndBookId(reviewDTO.getUserId(), reviewDTO.getBookId());

        if (reviewExists) {
            throw new RuntimeException("Review já existente");
        }

       
        Users user = usersRepository.findById(reviewDTO.getUserId())
            .orElseThrow(() -> new RuntimeException("User not found"));
        LocalDateTime now = LocalDateTime.now();
        Review review = new Review(user, reviewDTO.getBookId(), reviewDTO.getContent(), now);
        return reviewRepository.save(review);
    }

}
