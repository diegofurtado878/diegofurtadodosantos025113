package br.gov.mt.seplag.music_library_api.controller;

import br.gov.mt.seplag.music_library_api.entity.Artista;
import br.gov.mt.seplag.music_library_api.service.ArtistaService;
import br.gov.mt.seplag.music_library_api.dto.ArtistaRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/artistas")
public class ArtistaController {

    @Autowired
    private ArtistaService service;

    @GetMapping
    public List<Artista> listar() {
        return service.buscarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Artista> buscar(@PathVariable Integer id) {
        // O map é feito no Optional retornado pelo Service
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Artista criar(@RequestBody ArtistaRequestDTO dto) {
        Artista artista = new Artista();
        artista.setNome(dto.nome());
        return service.salvar(artista);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Artista> atualizar(@PathVariable Integer id, @RequestBody ArtistaRequestDTO dto) {
        return service.buscarPorId(id)
                .map(artistaExistente -> {
                    artistaExistente.setNome(dto.nome()); // Aqui o setNome vai funcionar
                    return ResponseEntity.ok(service.salvar(artistaExistente));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}