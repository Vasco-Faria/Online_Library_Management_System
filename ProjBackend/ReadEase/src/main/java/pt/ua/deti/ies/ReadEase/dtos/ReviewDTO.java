package pt.ua.deti.ies.ReadEase.dtos;

public class ReviewDTO {
    private Integer userId;
    private String bookId;
    private String content;

    public ReviewDTO(Integer userId,String bookId, String content) {
        this.userId=userId;
        this.bookId = bookId;
        this.content = content;
     
    }

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

   