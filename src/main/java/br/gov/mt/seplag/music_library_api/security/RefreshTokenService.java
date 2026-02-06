package br.gov.mt.seplag.music_library_api.security;

import br.gov.mt.seplag.music_library_api.entity.Token;
import br.gov.mt.seplag.music_library_api.entity.Usuario;
import br.gov.mt.seplag.music_library_api.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final TokenRepository tokenRepository;
    private final long refreshDays;

    public RefreshTokenService(TokenRepository tokenRepository,
                               @Value("${security.jwt.refreshTokenDays:7}") long refreshDays) {
        this.tokenRepository = tokenRepository;
        this.refreshDays = refreshDays;
    }

    public String generateRefreshToken(Usuario usuario) {
        String tokenValue = UUID.randomUUID().toString() + "." + UUID.randomUUID();
        Token t = new Token();
        t.setUsuario(usuario);
        t.setNomeToken(tokenValue);
        t.setThenToken(Instant.now().plus(refreshDays, ChronoUnit.DAYS));
        tokenRepository.save(t);
        return tokenValue;
    }

    public String validateAndGetLogin(String refreshToken) {
        return tokenRepository.findByNomeToken(refreshToken)
                .filter(t -> t.getThenToken().isAfter(Instant.now()))
                .map(t -> t.getUsuario().getLogin())
                .orElse(null);
    }
}
