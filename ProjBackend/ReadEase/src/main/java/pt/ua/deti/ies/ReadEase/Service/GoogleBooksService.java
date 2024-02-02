package pt.ua.deti.ies.ReadEase.Service;


import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class GoogleBooksService {

    @Value("${google.books.api.key}")
    private String apiKey;

     private final ResourceLoader resourceLoader;

    public GoogleBooksService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }



    private static final String PDF_FOLDER = "pdfs/";

     public String searchBooks(String query, String author) throws IOException, InterruptedException {
        String apiUrl = "https://www.googleapis.com/books/v1/volumes";

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(apiUrl)
                .queryParam("key", apiKey);

        if (query != null && !query.isEmpty()) {
            builder.queryParam("q", query);
        } else if (author != null && !author.isEmpty()) {
            builder.queryParam("q", "inauthor:" + author);
        }

        String url = builder.build(true).toUriString();

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }

    public String getBookDetails(String bookId) throws IOException, InterruptedException {
       
        String apiUrl = "https://www.googleapis.com/books/v1/volumes/" + bookId;
        
        
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

       
        if (response.statusCode() == 200) {
           
            return response.body();
        } else {
           
            throw new RuntimeException("Failed to fetch book details. HTTP Status Code: " + response.statusCode());
        }
    }

    public List<JSONObject> getWeeklyRecommendations(List<String> bookIds) {
    
        List<JSONObject> recommendations = new ArrayList<>();
        try {
            for (String bookId : bookIds) {
               
                String bookInfoJsonString = getBookDetails(bookId);
    
                JSONObject bookInfo = new JSONObject(bookInfoJsonString);
    
                recommendations.add(bookInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return recommendations;
    }


    
    public byte[] getRandomPdfBytes() throws IOException {
        List<Path> pdfFiles = listAllPdfFiles();
        if (!pdfFiles.isEmpty()) {
            Random random = new Random();
            Path randomPdfPath = pdfFiles.get(random.nextInt(pdfFiles.size()));
            return Files.readAllBytes(randomPdfPath);
        } else {
            throw new IOException("No PDF files found.");
        }
    }

    private List<Path> listAllPdfFiles() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:/pdfs");
        Path pdfDirectory = resource.getFile().toPath();
        return Files.walk(pdfDirectory)
                .filter(path -> path.toString().toLowerCase().endsWith(".pdf"))
                .collect(Collectors.toList());
    }



    
}   
