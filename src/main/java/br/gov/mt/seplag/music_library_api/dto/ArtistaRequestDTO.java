package br.gov.mt.seplag.music_library_api.dto;

import br.gov.mt.seplag.music_library_api.entity.TipoArtista;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ArtistaRequestDTO(
        @NotBlank String nome,
        @NotNull TipoArtista tipoArtista
) {}
