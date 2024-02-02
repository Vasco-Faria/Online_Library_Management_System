package pt.ua.deti.ies.ReadEase.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pt.ua.deti.ies.ReadEase.Service.ReviewService;
import pt.ua.deti.ies.ReadEase.dtos.ReviewDTO;
import pt.ua.deti.ies.ReadEase.model.Review;
import pt.ua.deti.ies.ReadEase.repository.ReviewRepository;


@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepository;

     @Autowired
    private ReviewService reviewService;


     @GetMapping("/all")
    public ResponseEntity<List<Review>> getAllReviews() {
        List<Review> reviews = reviewRepository.findAll();
        return ResponseEntity.ok(reviews);
    }

    @PostMapping("/add")
    public ResponseEntity<Review> addReview(@RequestBody ReviewDTO reviewDTO) {
        Review savedReview = reviewService.addReview(reviewDTO);
        return ResponseEntity.ok(savedReview);
    }



    @DeleteMapping("del/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable Long id) {
        if (reviewRepository.existsById(id)) {
            reviewRepository.deleteById(id);
            return ResponseEntity.ok().build(); 
        } else {
            return ResponseEntity.notFound().build(); 
        }
    }

    @GetMapping("/byBook/{bookId}")
    public ResponseEntity<List<Review>> getReviewsByBookId(@PathVariable String bookId) {
        List<Review> reviews = reviewRepository.findByBookId(bookId);
        return reviews.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(reviews);
    }

    
}
