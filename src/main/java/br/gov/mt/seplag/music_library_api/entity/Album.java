package br.gov.mt.seplag.music_library_api.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Entity
@Table(name = "ALBUM")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ALBUM")
    private Integer id; // Capitalized 'I'

    @Column(name = "TITULO_ALBUM", nullable = false)
    private String titulo;

    @ManyToMany(mappedBy = "albuns")
    private Set<Artista> artistas;
}