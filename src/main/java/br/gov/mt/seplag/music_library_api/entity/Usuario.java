package br.gov.mt.seplag.music_library_api.entity;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "USUARIO")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_USUARIO")
    private Integer id;

    @Column(name = "LOGIN_USUARIO", nullable = false, unique = true)
    private String login;

    @Column(name = "PASSWORD_USUARIO", length = 2048)
    private String password;

    @Column(name = "KEY_USUARIO", length = 500)
    private String keyUsuario;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() { return password; }

    @Override
    public String getUsername() { return login; }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public void setPassword(String password) { this.password = password; }

    public String getKeyUsuario() { return keyUsuario; }
    public void setKeyUsuario(String keyUsuario) { this.keyUsuario = keyUsuario; }

    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return true; }
}
