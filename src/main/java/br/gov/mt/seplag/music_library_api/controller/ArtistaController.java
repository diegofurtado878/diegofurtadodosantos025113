package br.gov.mt.seplag.music_library_api.controller;

import br.gov.mt.seplag.music_library_api.dto.ArtistaRequestDTO;
import br.gov.mt.seplag.music_library_api.dto.ArtistaResponseDTO;
import br.gov.mt.seplag.music_library_api.entity.TipoArtista;
import br.gov.mt.seplag.music_library_api.service.ArtistaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/artistas")
@RequiredArgsConstructor
@Tag(name = "Artistas", description = "Gerenciamento de artistas - versão v1")
public class ArtistaController {

    private final ArtistaService service;

    @GetMapping
    @Operation(summary = "Lista artistas com paginação e filtros")
    public ResponseEntity<Page<ArtistaResponseDTO>> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) TipoArtista tipoArtista,
            @PageableDefault(size = 10, sort = "nome") Pageable pageable
    ) {
        return ResponseEntity.ok(service.buscarTodosPaginado(nome, tipoArtista, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca artista por ID")
    public ResponseEntity<ArtistaResponseDTO> buscar(@PathVariable Integer id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Cria um artista")
    public ResponseEntity<ArtistaResponseDTO> criar(@RequestBody @Valid ArtistaRequestDTO dto) {
        return ResponseEntity.ok(service.criar(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza artista")
    public ResponseEntity<ArtistaResponseDTO> atualizar(
            @PathVariable Integer id,
            @RequestBody @Valid ArtistaRequestDTO dto
    ) {
        return service.atualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove artista")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
