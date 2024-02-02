package pt.ua.deti.ies.ReadEase.dtos;

import lombok.Data;

@Data
public class ApiResponse {
    private String message;

    public ApiResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}