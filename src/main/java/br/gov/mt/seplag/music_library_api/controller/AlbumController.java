package br.gov.mt.seplag.music_library_api.controller;

import br.gov.mt.seplag.music_library_api.dto.AlbumRequestDTO;
import br.gov.mt.seplag.music_library_api.dto.AlbumResponseDTO;
import br.gov.mt.seplag.music_library_api.service.AlbumService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/albuns")
@RequiredArgsConstructor
@Tag(name = "Álbuns", description = "Gerenciamento de álbuns da biblioteca")
public class AlbumController {

    private final AlbumService albumService;

    @PostMapping
    public ResponseEntity<AlbumResponseDTO> criar(@RequestBody @Valid AlbumRequestDTO dto) {
        return ResponseEntity.ok(albumService.criar(dto));
    }

    @GetMapping
    public ResponseEntity<List<AlbumResponseDTO>> listar() {
        return ResponseEntity.ok(albumService.listarTodos());
    }
}