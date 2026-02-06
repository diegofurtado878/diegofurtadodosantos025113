package br.gov.mt.seplag.music_library_api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ALBUM")
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ALBUM")
    private Integer id;

    @Column(name = "TITULO_ALBUM", nullable = false)
    private String titulo;

    @Column(name = "KEY_OBJECT_MINIO", length = 500)
    private String keyObjectMinio;

    @JsonIgnore
    @ManyToMany(mappedBy = "albuns")
    private Set<Artista> artistas = new HashSet<>();

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getKeyObjectMinio() { return keyObjectMinio; }
    public void setKeyObjectMinio(String keyObjectMinio) { this.keyObjectMinio = keyObjectMinio; }

    public Set<Artista> getArtistas() { return artistas; }
    public void setArtistas(Set<Artista> artistas) { this.artistas = artistas; }
}
