package pt.ua.deti.ies.ReadEase.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pt.ua.deti.ies.ReadEase.model.Favoritos;
import pt.ua.deti.ies.ReadEase.model.Users;
import pt.ua.deti.ies.ReadEase.repository.FavoritosRepository;
import pt.ua.deti.ies.ReadEase.repository.UsersRepository;

@Service
public class FavoritosService {

    @Autowired
    private FavoritosRepository favoritosRepository;

    @Autowired
    private UsersRepository userRepository;

    public void addFavorito(Integer userId, String BookId) {
        Users user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        Favoritos favorito = new Favoritos();
        favorito.setUser(user);
        favorito.setBook(BookId);
        favoritosRepository.save(favorito);
    } 

    public void removeFavorito(Integer userId, String BookId) {
        Users user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        Favoritos favorito = favoritosRepository.findByUserAndBookId(user, BookId)
                .orElseThrow(() -> new RuntimeException("Favorito não encontrado"));
        favoritosRepository.delete(favorito);
    }

    public List<String> getFavoritos(Integer userId) {
        Users user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        List<Favoritos> favoritos = favoritosRepository.findByUser(user);
        return favoritos.stream()
                        .map(Favoritos::getBook)
                        .collect(Collectors.toList());
    }
}
