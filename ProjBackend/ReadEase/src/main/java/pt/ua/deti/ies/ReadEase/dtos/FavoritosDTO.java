package pt.ua.deti.ies.ReadEase.dtos;

import lombok.Data;

@Data
public class FavoritosDTO {
    private Integer userId;
    private String bookId; 

   

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }
}

  
