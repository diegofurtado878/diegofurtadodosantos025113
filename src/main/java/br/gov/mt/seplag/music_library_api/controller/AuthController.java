package br.gov.mt.seplag.music_library_api.controller;

import br.gov.mt.seplag.music_library_api.dto.AuthenticationDTO;
import br.gov.mt.seplag.music_library_api.dto.ErrorResponseDTO;
import br.gov.mt.seplag.music_library_api.dto.LoginResponseDTO;
import br.gov.mt.seplag.music_library_api.entity.Usuario;
import br.gov.mt.seplag.music_library_api.repository.UsuarioRepository;
import br.gov.mt.seplag.music_library_api.security.RefreshTokenService;
import br.gov.mt.seplag.music_library_api.security.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "Login, registro e renovação de token")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final RefreshTokenService refreshTokenService;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager,
                          TokenService tokenService,
                          RefreshTokenService refreshTokenService,
                          UsuarioRepository usuarioRepository,
                          PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.refreshTokenService = refreshTokenService;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    @Operation(summary = "Registrar novo usuário", description = "Cria conta e retorna tokens de acesso")
    public ResponseEntity<?> register(@RequestBody @Valid AuthenticationDTO dto) {
        if (usuarioRepository.existsByLogin(dto.login())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponseDTO(409, "Conflict", "Login já em uso"));
        }
        Usuario usuario = new Usuario();
        usuario.setLogin(dto.login());
        usuario.setPassword(passwordEncoder.encode(dto.senha()));
        usuario = usuarioRepository.save(usuario);
        String token = tokenService.generateToken(usuario);
        String refreshToken = refreshTokenService.generateRefreshToken(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(new LoginResponseDTO(token, refreshToken));
    }

    @PostMapping("/login")
    @Operation(summary = "Login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthenticationDTO dto) {
        try {
            var authToken = new UsernamePasswordAuthenticationToken(dto.login(), dto.senha());
            var auth = authenticationManager.authenticate(authToken);
            var usuario = (Usuario) auth.getPrincipal();
            String token = tokenService.generateToken(usuario);
            String refreshToken = refreshTokenService.generateRefreshToken(usuario);
            return ResponseEntity.ok(new LoginResponseDTO(token, refreshToken));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponseDTO(401, "Unauthorized", "Credenciais inválidas"));
        }
    }

    @PostMapping("/refresh")
    @Operation(summary = "Renovar token de acesso")
    public ResponseEntity<?> refresh(@RequestParam("refresh_token") String refreshToken) {
        String login = refreshTokenService.validateAndGetLogin(refreshToken);
        if (login == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponseDTO(401, "Unauthorized", "Refresh token inválido ou expirado"));
        }
        Usuario usuario = new Usuario();
        usuario.setLogin(login);
        String newToken = tokenService.generateToken(usuario);
        return ResponseEntity.ok(new LoginResponseDTO(newToken, refreshToken));
    }
}
