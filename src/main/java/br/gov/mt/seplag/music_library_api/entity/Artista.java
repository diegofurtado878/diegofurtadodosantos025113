package br.gov.mt.seplag.music_library_api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ARTISTA")
public class Artista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ARTISTA")
    private Integer id;

    @Column(name = "NOME_ARTISTA", nullable = false)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(name = "TIPO_ARTISTA")
    private TipoArtista tipoArtista;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "ARTISTA_ALBUM",
            joinColumns = @JoinColumn(name = "ID_ARTISTA"),
            inverseJoinColumns = @JoinColumn(name = "ID_ALBUM")
    )
    private Set<Album> albuns = new HashSet<>();

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public TipoArtista getTipoArtista() { return tipoArtista; }
    public void setTipoArtista(TipoArtista tipoArtista) { this.tipoArtista = tipoArtista; }

    public Set<Album> getAlbuns() { return albuns; }
    public void setAlbuns(Set<Album> albuns) { this.albuns = albuns; }
}
