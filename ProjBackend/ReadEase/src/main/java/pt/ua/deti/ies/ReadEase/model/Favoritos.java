package pt.ua.deti.ies.ReadEase.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Favoritos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    private String bookId;

    

public Long getId() {
    return id;
}

public void setId(Long id) {
    this.id = id;
}

public Users getUser() {
    return user;
}

public void setUser(Users user) {
    this.user = user;
}

public String getBook() {
    return bookId;
}

public void setBook(String book) {
    this.bookId = book;
}
}