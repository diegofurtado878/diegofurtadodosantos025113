package br.gov.mt.seplag.music_library_api.repository;

import br.gov.mt.seplag.music_library_api.entity.Artista;
import br.gov.mt.seplag.music_library_api.entity.TipoArtista;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistaRepository extends JpaRepository<Artista, Integer> {

    Page<Artista> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
    Page<Artista> findByTipoArtista(TipoArtista tipo, Pageable pageable);
    Page<Artista> findByTipoArtistaAndNomeContainingIgnoreCase(TipoArtista tipo, String nome, Pageable pageable);
}
