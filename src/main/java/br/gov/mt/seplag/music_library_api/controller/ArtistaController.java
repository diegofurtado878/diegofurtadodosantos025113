package br.gov.mt.seplag.music_library_api.controller;

import br.gov.mt.seplag.music_library_api.dto.ArtistaRequestDTO;
import br.gov.mt.seplag.music_library_api.dto.ArtistaResponseDTO;
import br.gov.mt.seplag.music_library_api.service.ArtistaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/artistas")
@RequiredArgsConstructor
public class ArtistaController {

    private final ArtistaService service;

    @GetMapping
    public ResponseEntity<List<ArtistaResponseDTO>> listar() {
        return ResponseEntity.ok(service.buscarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArtistaResponseDTO> buscar(@PathVariable Integer id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ArtistaResponseDTO> criar(@RequestBody @Valid ArtistaRequestDTO dto) {
        return ResponseEntity.ok(service.criar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArtistaResponseDTO> atualizar(@PathVariable Integer id,
                                                        @RequestBody @Valid ArtistaRequestDTO dto) {
        return service.atualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}