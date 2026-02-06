package br.gov.mt.seplag.music_library_api.service;

import br.gov.mt.seplag.music_library_api.dto.ArtistaRequestDTO;
import br.gov.mt.seplag.music_library_api.dto.ArtistaResponseDTO;
import br.gov.mt.seplag.music_library_api.entity.Artista;
import br.gov.mt.seplag.music_library_api.entity.TipoArtista;
import br.gov.mt.seplag.music_library_api.repository.ArtistaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ArtistaService {

    private final ArtistaRepository repository;

    public ArtistaService(ArtistaRepository repository) {
        this.repository = repository;
    }

    public Page<ArtistaResponseDTO> buscarTodosPaginado(String nome, TipoArtista tipo, Pageable pageable) {
        boolean hasNome = nome != null && !nome.isBlank();
        Page<Artista> page;
        if (tipo != null && hasNome) {
            page = repository.findByTipoArtistaAndNomeContainingIgnoreCase(tipo, nome, pageable);
        } else if (tipo != null) {
            page = repository.findByTipoArtista(tipo, pageable);
        } else if (hasNome) {
            page = repository.findByNomeContainingIgnoreCase(nome, pageable);
        } else {
            page = repository.findAll(pageable);
        }
        return page.map(this::toResponse);
    }

    public ArtistaResponseDTO criar(ArtistaRequestDTO dto) {
        Artista a = new Artista();
        a.setNome(dto.nome());
        a.setTipoArtista(dto.tipoArtista());
        return toResponse(repository.save(a));
    }

    public java.util.Optional<ArtistaResponseDTO> buscarPorId(Integer id) {
        return repository.findById(id).map(this::toResponse);
    }

    public java.util.Optional<ArtistaResponseDTO> atualizar(Integer id, ArtistaRequestDTO dto) {
        return repository.findById(id)
                .map(a -> {
                    a.setNome(dto.nome());
                    a.setTipoArtista(dto.tipoArtista());
                    return toResponse(repository.save(a));
                });
    }

    public void excluir(Integer id) {
        Artista a = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Artista não encontrado"));
        a.getAlbuns().clear();
        repository.save(a);
        repository.delete(a);
    }

    private ArtistaResponseDTO toResponse(Artista a) {
        return new ArtistaResponseDTO(a.getId(), a.getNome(), a.getTipoArtista());
    }
}
