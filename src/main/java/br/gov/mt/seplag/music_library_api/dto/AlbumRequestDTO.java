package br.gov.mt.seplag.music_library_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AlbumRequestDTO(
        @NotBlank(message = "O título do álbum é obrigatório")
        @Size(min = 1, max = 255, message = "O título deve ter entre 1 e 255 caracteres")
        String titulo
) {
}