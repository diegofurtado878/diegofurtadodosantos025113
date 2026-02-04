package br.gov.mt.seplag.music_library_api.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "ARTISTA")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Artista {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ARTISTA")
    private Integer id;

    @Column(name = "NOME_ARTISTA", nullable = false)
    private String nome;

    @ManyToMany
    @JoinTable(
            name = "ARTISTA_ALBUM",
            joinColumns = @JoinColumn(name = "ID_ARTISTA"),
            inverseJoinColumns = @JoinColumn(name = "ID_ALBUM")
    )
    private List<Album> albuns;
}