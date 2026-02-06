package br.gov.mt.seplag.music_library_api.repository;

import br.gov.mt.seplag.music_library_api.entity.Album;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlbumRepository extends JpaRepository<Album, Integer> {

    Page<Album> findByArtistas_Id(Integer idArtista, Pageable pageable);

    @EntityGraph(attributePaths = "artistas")
    Optional<Album> findWithArtistasById(Integer id);
}
