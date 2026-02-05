package br.gov.mt.seplag.music_library_api.service;

import br.gov.mt.seplag.music_library_api.dto.AlbumRequestDTO;
import br.gov.mt.seplag.music_library_api.entity.Album;
import br.gov.mt.seplag.music_library_api.repository.AlbumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlbumService {

    private final AlbumRepository albumRepository;

    public Album salvar(AlbumRequestDTO dto) {
        Album album = Album.builder()
                .titulo(dto.titulo())
                .build();
        return albumRepository.save(album);
    }

    public List<Album> listarTodos() {
        return albumRepository.findAll();
    }
}