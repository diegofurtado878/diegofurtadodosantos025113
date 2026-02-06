package br.gov.mt.seplag.music_library_api.security;

import br.gov.mt.seplag.music_library_api.dto.AuthenticationDTO;
import br.gov.mt.seplag.music_library_api.dto.LoginResponseDTO;
import br.gov.mt.seplag.music_library_api.entity.Usuario;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final RefreshTokenService refreshTokenService;

    public AuthController(AuthenticationManager authenticationManager,
                          TokenService tokenService,
                          RefreshTokenService refreshTokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthenticationDTO dto) {
        try {
            var authToken = new UsernamePasswordAuthenticationToken(dto.login(), dto.senha());
            var auth = authenticationManager.authenticate(authToken);
            var usuario = (Usuario) auth.getPrincipal();
            String token = tokenService.generateToken(usuario);
            String refreshToken = refreshTokenService.generateRefreshToken(usuario);
            return ResponseEntity.ok(new LoginResponseDTO(token, refreshToken));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas");
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestParam("refresh_token") String refreshToken) {
        String login = refreshTokenService.validateAndGetLogin(refreshToken);
        if (login == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token inválido ou expirado");
        }
        Usuario usuario = new Usuario();
        usuario.setLogin(login);
        String newToken = tokenService.generateToken(usuario);
        return ResponseEntity.ok(new LoginResponseDTO(newToken, refreshToken));
    }
}
