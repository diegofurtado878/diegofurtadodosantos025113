package br.gov.mt.seplag.music_library_api.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "ALBUM")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ALBUM")
    private Integer id;

    @Column(name = "TITULO_ALBUM", nullable = false)
    private String titulo;

    @ManyToMany(mappedBy = "albuns")
    private List<Artista> artistas;
}
