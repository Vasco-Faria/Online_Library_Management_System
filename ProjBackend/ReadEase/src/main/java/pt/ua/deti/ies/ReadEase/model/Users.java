package pt.ua.deti.ies.ReadEase.model;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;
import java.util.Set;


@Entity
@Table(name = "ReadEase.Users")

public class Users {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="nome",nullable = false)
    private String nome;

    @Column(name="email",nullable = false)
    private String email;

    @JsonIgnore
    @Column(name="senha")
    private String senha;

    @Column(name="tipo")
    private String tipo;

    @Column(name="telemovel")
    private String telemovel;

    @Column(name="data_de_nascimento")
    private LocalDate data_de_nascimento;

    @OneToMany(mappedBy = "user")
    private List<RoomReserves> reservations;

    @Column(name="is_verified", nullable = false)
    private boolean isVerified;

    @OneToMany(mappedBy = "user")
    private Set<Favoritos> favoritos;


    @OneToMany(mappedBy = "user")
    private Set<Review> reviews;

    @OneToMany(mappedBy = "user")
    private Set<Notification> notifications;

    public Users() {
        
    }

    public Users(int id,String nome, String email,String senha, String tipo, String telemovel, LocalDate data_de_nascimento) {
        this.id=id;
        this.nome = nome;
        this.email = email;
        this.senha=senha;
        this.tipo = tipo;
        this.telemovel = telemovel;
        this.data_de_nascimento = data_de_nascimento;
        this.isVerified=false;
    }

    public Users(int id,String nome, String email, LocalDate data_de_nascimento) {
        this.nome = nome;
        this.email = email;
        this.data_de_nascimento=data_de_nascimento;
        this.isVerified=false;
    }
    

    public Users(int userId, String name, String email2, String senha2, String tipo2, String phoneNumber,LocalDate birthdate, boolean isVerified2) {
        this.id=userId;
        this.nome=name;
        this.email=email2;
        this.senha=senha2;
        this.tipo=tipo2;
        this.telemovel=phoneNumber;
        this.data_de_nascimento=birthdate;
        this.isVerified=isVerified2;

    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public String getTipo() {
        return tipo;
    }

    public String getTelemovel() {
        return telemovel;
    }

    public LocalDate getData_de_nascimento() {
        return data_de_nascimento;
    }

//metodos set
    
    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setTelemovel(String telemovel) {
        this.telemovel = telemovel;
    }

    public void setData_de_nascimento(LocalDate data_de_nascimento) {
        this.data_de_nascimento = data_de_nascimento;
    }

    public Users orElse(Object object) {
        return null;
    }

    public void setVerified(Boolean vBoolean){
        this.isVerified=vBoolean;
    }

    public Boolean getVerified(){
        return isVerified;
    }

}
