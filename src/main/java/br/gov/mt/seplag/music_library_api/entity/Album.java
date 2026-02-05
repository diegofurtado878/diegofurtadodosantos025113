package br.gov.mt.seplag.music_library_api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private Integer id;

    @Column(name = "TITULO_ALBUM", nullable = false)
    private String titulo;

    /**
     * Lado inverso da relação ManyToMany.
     * Também é ignorado na serialização direta da entidade para evitar recursão.
     * A exposição via API será feita por DTOs específicos.
     */
    @ManyToMany(mappedBy = "albuns")
    @JsonIgnore
    private Set<Artista> artistas;
}