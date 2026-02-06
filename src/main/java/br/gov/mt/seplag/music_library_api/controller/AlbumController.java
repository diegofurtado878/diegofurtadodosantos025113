package br.gov.mt.seplag.music_library_api.controller;

import br.gov.mt.seplag.music_library_api.dto.AlbumRequestDTO;
import br.gov.mt.seplag.music_library_api.dto.AlbumResponseDTO;
import br.gov.mt.seplag.music_library_api.service.AlbumService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/v1/albuns")
@RequiredArgsConstructor
@Tag(name = "Álbuns", description = "Gerenciamento de álbuns - versão v1")
public class AlbumController {

    private final AlbumService albumService;

    @PostMapping
    @Operation(summary = "Cria um álbum e associa artistas")
    public ResponseEntity<AlbumResponseDTO> criar(@RequestBody @Valid AlbumRequestDTO dto) {
        return ResponseEntity.ok(albumService.criar(dto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca álbum por ID")
    public ResponseEntity<AlbumResponseDTO> buscarPorId(@PathVariable Integer id) {
        return albumService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza título e artistas do álbum")
    public ResponseEntity<AlbumResponseDTO> atualizar(
            @PathVariable Integer id,
            @RequestBody @Valid AlbumRequestDTO dto
    ) {
        return albumService.atualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove álbum")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        albumService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/{id}/capas", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload de capas do álbum (MinIO)")
    public ResponseEntity<Void> uploadCapas(
            @PathVariable Integer id,
            @RequestPart("arquivos") List<MultipartFile> arquivos
    ) {
        albumService.uploadCapas(id, arquivos);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/{id}/capas")
    @Operation(summary = "Lista URLs pré-assinadas das capas (30 min)")
    public ResponseEntity<List<String>> listarCapas(@PathVariable Integer id) {
        return ResponseEntity.ok(albumService.listarCapasUrls(id));
    }

    @GetMapping
    @Operation(summary = "Lista álbuns com paginação")
    public ResponseEntity<Page<AlbumResponseDTO>> listar(
            @Parameter(description = "Filtro por nome do artista") @RequestParam(required = false) String nomeArtista,
            @Parameter(description = "Filtro por tipo do artista") @RequestParam(required = false) String tipoArtista,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        return ResponseEntity.ok(albumService.listarPaginado(nomeArtista, tipoArtista, pageable));
    }
}
