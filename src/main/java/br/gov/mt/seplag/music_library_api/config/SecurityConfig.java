package br.gov.mt.seplag.music_library_api.config;

import br.gov.mt.seplag.music_library_api.repository.UsuarioRepository;
import br.gov.mt.seplag.music_library_api.security.SecurityFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final SecurityFilter securityFilter;
    private final RateLimitFilter rateLimitFilter;
    private final UsuarioRepository usuarioRepository;

    public SecurityConfig(SecurityFilter securityFilter, RateLimitFilter rateLimitFilter, UsuarioRepository usuarioRepository) {
        this.securityFilter = securityFilter;
        this.rateLimitFilter = rateLimitFilter;
        this.usuarioRepository = usuarioRepository;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> {})
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/api-docs/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/login", "/auth/refresh").permitAll()
                        .requestMatchers("/actuator/health", "/actuator/health/**").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((req, res, e) -> {
                            res.setStatus(HttpStatus.UNAUTHORIZED.value());
                            res.setContentType("application/json;charset=UTF-8");
                            res.getWriter().write("{\"status\":401,\"error\":\"Unauthorized\",\"message\":\"Credenciais inválidas ou ausentes\"}");
                        })
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(rateLimitFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            UserDetails user = usuarioRepository.findByLogin(username);
            if (user == null) throw new UsernameNotFoundException("Usuário não encontrado: " + username);
            return user;
        };
    }
}
