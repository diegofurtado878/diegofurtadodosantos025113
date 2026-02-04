package br.gov.mt.seplag.music_library_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ArtistaRequestDTO(
        @NotBlank(message = "O nome do artista é obrigatório")
        @Size(min = 2, max = 255, message = "O nome deve ter entre 2 e 255 caracteres")
        String nome
) {
}