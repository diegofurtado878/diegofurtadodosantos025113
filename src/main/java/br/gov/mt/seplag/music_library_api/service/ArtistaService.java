package br.gov.mt.seplag.music_library_api.service;

import br.gov.mt.seplag.music_library_api.dto.AlbumResumoDTO;
import br.gov.mt.seplag.music_library_api.dto.ArtistaRequestDTO;
import br.gov.mt.seplag.music_library_api.dto.ArtistaResponseDTO;
import br.gov.mt.seplag.music_library_api.entity.Album;
import br.gov.mt.seplag.music_library_api.entity.Artista;
import br.gov.mt.seplag.music_library_api.repository.ArtistaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArtistaService {

    private final ArtistaRepository repository;

    public List<ArtistaResponseDTO> buscarTodos() {
        return repository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<ArtistaResponseDTO> buscarPorId(Integer id) {
        return repository.findById(id)
                .map(this::toResponseDTO);
    }

    public ArtistaResponseDTO criar(ArtistaRequestDTO dto) {
        Artista artista = new Artista();
        artista.setNome(dto.nome());
        Artista salvo = repository.save(artista);
        return toResponseDTO(salvo);
    }

    public Optional<ArtistaResponseDTO> atualizar(Integer id, ArtistaRequestDTO dto) {
        return repository.findById(id)
                .map(existente -> {
                    existente.setNome(dto.nome());
                    Artista salvo = repository.save(existente);
                    return toResponseDTO(salvo);
                });
    }

    private ArtistaResponseDTO toResponseDTO(Artista artista) {
        List<AlbumResumoDTO> albuns = artista.getAlbuns() == null ? List.of() :
                artista.getAlbuns().stream()
                        .map(this::toAlbumResumoDTO)
                        .collect(Collectors.toList());
        return new ArtistaResponseDTO(artista.getId(), artista.getNome(), albuns);
    }

    private AlbumResumoDTO toAlbumResumoDTO(Album album) {
        return new AlbumResumoDTO(album.getId(), album.getTitulo());
    }
}