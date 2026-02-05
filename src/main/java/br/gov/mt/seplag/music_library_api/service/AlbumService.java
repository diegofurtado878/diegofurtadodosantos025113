package br.gov.mt.seplag.music_library_api.service;

import br.gov.mt.seplag.music_library_api.dto.AlbumRequestDTO;
import br.gov.mt.seplag.music_library_api.dto.AlbumResponseDTO;
import br.gov.mt.seplag.music_library_api.dto.ArtistaResumoDTO;
import br.gov.mt.seplag.music_library_api.entity.Album;
import br.gov.mt.seplag.music_library_api.entity.Artista;
import br.gov.mt.seplag.music_library_api.repository.AlbumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlbumService {

    private final AlbumRepository albumRepository;

    public AlbumResponseDTO criar(AlbumRequestDTO dto) {
        Album album = Album.builder()
                .titulo(dto.titulo())
                .build();
        Album salvo = albumRepository.save(album);
        return toResponseDTO(salvo);
    }

    public List<AlbumResponseDTO> listarTodos() {
        return albumRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    private AlbumResponseDTO toResponseDTO(Album album) {
        List<ArtistaResumoDTO> artistas = album.getArtistas() == null ? List.of() :
                album.getArtistas().stream()
                        .map(this::toArtistaResumoDTO)
                        .collect(Collectors.toList());
        return new AlbumResponseDTO(album.getId(), album.getTitulo(), artistas);
    }

    private ArtistaResumoDTO toArtistaResumoDTO(Artista artista) {
        return new ArtistaResumoDTO(artista.getId(), artista.getNome());
    }
}