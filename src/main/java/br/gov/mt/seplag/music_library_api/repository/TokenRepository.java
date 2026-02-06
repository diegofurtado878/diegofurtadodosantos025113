package br.gov.mt.seplag.music_library_api.repository;

import br.gov.mt.seplag.music_library_api.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByNomeToken(String nomeToken);
}
