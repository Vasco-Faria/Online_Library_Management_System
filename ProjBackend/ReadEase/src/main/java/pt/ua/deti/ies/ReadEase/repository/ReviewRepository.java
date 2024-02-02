package pt.ua.deti.ies.ReadEase.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pt.ua.deti.ies.ReadEase.model.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByBookId(String bookId);

    boolean existsByUserIdAndBookId(Integer userId, String bookId);
    
    
}
