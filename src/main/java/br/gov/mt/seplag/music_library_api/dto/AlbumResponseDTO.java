package br.gov.mt.seplag.music_library_api.dto;

import java.util.List;

public record AlbumResponseDTO(
        Integer id,
        String titulo,
        List<ArtistaResumoDTO> artistas
) {
}

