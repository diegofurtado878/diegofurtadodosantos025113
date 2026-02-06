package br.gov.mt.seplag.music_library_api.repository;

import br.gov.mt.seplag.music_library_api.entity.AlbumImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlbumImageRepository extends JpaRepository<AlbumImage, Long> {

    List<AlbumImage> findByAlbum_Id(Integer albumId);
}
