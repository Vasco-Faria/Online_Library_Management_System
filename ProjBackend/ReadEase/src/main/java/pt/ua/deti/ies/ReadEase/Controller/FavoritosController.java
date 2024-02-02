package pt.ua.deti.ies.ReadEase.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pt.ua.deti.ies.ReadEase.Service.FavoritosService;
import pt.ua.deti.ies.ReadEase.dtos.FavoritosDTO;

@RestController
@RequestMapping("/favorites")
public class FavoritosController {

    @Autowired
    private FavoritosService favoritosService;

    @PostMapping("/add")
    public ResponseEntity<?> addFavorito(@RequestBody FavoritosDTO favoritoDTO) {
        try {
            favoritosService.addFavorito(favoritoDTO.getUserId(), favoritoDTO.getBookId());
            return ResponseEntity.ok("Favorito adicionado com sucesso");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao adicionar favorito");
        }
    }

    @PostMapping("/remove")
    public ResponseEntity<?> removeFavorito(@RequestBody FavoritosDTO favoritoDTO) {
        try {
            favoritosService.removeFavorito(favoritoDTO.getUserId(), favoritoDTO.getBookId());
            return ResponseEntity.ok("Favorito removido com sucesso");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao remover favorito");
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getFavoritos(@PathVariable Integer userId) {
        try {
            List<String> favoritos = favoritosService.getFavoritos(userId);
            if (!favoritos.isEmpty()) {
                return ResponseEntity.ok(favoritos);
            } else {
                return ResponseEntity.noContent().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao obter favoritos");
        }
    }
}