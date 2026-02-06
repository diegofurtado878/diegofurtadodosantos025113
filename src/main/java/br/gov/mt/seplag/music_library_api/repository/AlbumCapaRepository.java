package br.gov.mt.seplag.music_library_api.repository;

import br.gov.mt.seplag.music_library_api.entity.AlbumCapa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlbumCapaRepository extends JpaRepository<AlbumCapa, Integer> {

    List<AlbumCapa> findByAlbum_Id(Integer idAlbum);
}
