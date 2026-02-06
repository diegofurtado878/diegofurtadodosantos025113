package br.gov.mt.seplag.music_library_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record AlbumRequestDTO(
        @NotBlank String titulo,
        @NotEmpty List<Integer> artistasIds
) {}
