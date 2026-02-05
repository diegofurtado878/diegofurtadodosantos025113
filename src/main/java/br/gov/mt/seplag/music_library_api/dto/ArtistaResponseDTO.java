package br.gov.mt.seplag.music_library_api.dto;

import java.util.List;

public record ArtistaResponseDTO(
        Integer id,
        String nome,
        List<AlbumResumoDTO> albuns
) {
}

