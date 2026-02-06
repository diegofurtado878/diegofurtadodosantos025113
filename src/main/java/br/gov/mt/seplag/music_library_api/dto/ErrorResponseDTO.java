package br.gov.mt.seplag.music_library_api.dto;

public record ErrorResponseDTO(int status, String error, String message) {}
