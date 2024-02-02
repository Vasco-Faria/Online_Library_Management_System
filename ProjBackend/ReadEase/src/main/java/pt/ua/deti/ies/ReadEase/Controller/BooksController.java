package pt.ua.deti.ies.ReadEase.Controller;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import pt.ua.deti.ies.ReadEase.Service.BookReservationService;
import pt.ua.deti.ies.ReadEase.Service.GoogleBooksService;


import java.io.IOException;
import java.util.List;



@RestController
@RequestMapping("/books")
public class BooksController {

    @Autowired
    private GoogleBooksService googleBooksService;

    @Autowired
    private BookReservationService bookReservationService;

    @GetMapping(value = "/search", produces = "application/json")
    @ResponseBody
    public String searchBooks(@RequestParam(required = false) String query, @RequestParam(required = false) String author) {
        try {
            String booksApiResponse;

            if (query != null) {
                query = query.replace(" ", "%20");
            }

              if (author != null) {
                author = author.replace(" ", "%20");
            }

        
            booksApiResponse = googleBooksService.searchBooks(query, author);
            
          
            JSONObject jsonResponse = new JSONObject(booksApiResponse);
    
            
            JSONArray items = jsonResponse.getJSONArray("items");
    
            
            for (int i = 0; i < items.length(); i++) {
                JSONObject book = items.getJSONObject(i);
                String bookId = book.getString("id");
    
               
                boolean isAvailable = bookReservationService.checkBookAvailability(bookId);
    
                
                book.put("isAvailable", isAvailable);
            }
            
            return jsonResponse.toString();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "{\"error\": \"Error while searching for books\"}";
        }
    }
    

    @GetMapping(value = "/details/{id}", produces = "application/json")
    @ResponseBody
    public String getBookDetails(@PathVariable String id) {
        try {
            String bookDetailsResponse = googleBooksService.getBookDetails(id);
            JSONObject jsonResponse = new JSONObject(bookDetailsResponse);

            boolean isAvailable = bookReservationService.checkBookAvailability(id);

            
            jsonResponse.put("isAvailable", isAvailable);

            return jsonResponse.toString();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "{\"error\": \"Error while fetching book details\"}";
        }
    }



    @GetMapping(value = "/weekly-recommendations", produces = "application/json")
    @ResponseBody
    public String getWeeklyRecommendations() {
        try {
            List<String> bookIds = List.of("Hf_UBQAAQBAJ", "zIZGEAAAQBAJ", "kvwk9dSuv-8C");

            List<JSONObject> bookInformationList = googleBooksService.getWeeklyRecommendations(bookIds);

            for (JSONObject bookInfo : bookInformationList) {
                String bookId = bookInfo.getString("id");
    
                boolean isAvailable = bookReservationService.checkBookAvailability(bookId);
    
                bookInfo.put("isAvailable", isAvailable);
            }

            JSONArray jsonArray = new JSONArray(bookInformationList);

            return jsonArray.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\": \"Error while getting weekly recommendations\"}";
        }
    }


    @GetMapping("/pdf/download")
    public ResponseEntity<ByteArrayResource> downloadRandomPdf() {
        try {
            byte[] pdfBytes = googleBooksService.getRandomPdfBytes();

            ByteArrayResource resource = new ByteArrayResource(pdfBytes);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "random.pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(pdfBytes.length)
                    .body(resource);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }
}


