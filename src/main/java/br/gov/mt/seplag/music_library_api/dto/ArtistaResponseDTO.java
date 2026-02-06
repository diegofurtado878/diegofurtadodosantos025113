package br.gov.mt.seplag.music_library_api.dto;

import br.gov.mt.seplag.music_library_api.entity.TipoArtista;

public record ArtistaResponseDTO(
        Integer id,
        String nome,
        TipoArtista tipoArtista
) {}
