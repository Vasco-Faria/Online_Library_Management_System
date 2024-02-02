package pt.ua.deti.ies.ReadEase.dtos;

import lombok.Data;

@Data
public class EbookReservationDTO {
   
    private String ebookId;
    private int userId;

    public String getEbookId() {
        return ebookId;
    }

    public void setEbookId(String EbookId) {
        this.ebookId = EbookId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

}

