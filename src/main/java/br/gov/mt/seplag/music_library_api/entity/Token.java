package br.gov.mt.seplag.music_library_api.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "TOKENS")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_TOKENS")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_USUARIO", nullable = false)
    private Usuario usuario;

    @Column(name = "NOME_TOKEN", nullable = false, unique = true, length = 500)
    private String nomeToken;

    @Column(name = "THEN_TOKEN", nullable = false)
    private Instant thenToken;

    public Long getId() { return id; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public String getNomeToken() { return nomeToken; }
    public void setNomeToken(String nomeToken) { this.nomeToken = nomeToken; }
    public Instant getThenToken() { return thenToken; }
    public void setThenToken(Instant thenToken) { this.thenToken = thenToken; }
}
