package br.gov.mt.seplag.music_library_api.service;

import br.gov.mt.seplag.music_library_api.dto.AlbumRequestDTO;
import br.gov.mt.seplag.music_library_api.dto.AlbumResponseDTO;
import br.gov.mt.seplag.music_library_api.dto.ArtistaResumoDTO;
import br.gov.mt.seplag.music_library_api.entity.Album;
import br.gov.mt.seplag.music_library_api.entity.AlbumImage;
import br.gov.mt.seplag.music_library_api.entity.Artista;
import br.gov.mt.seplag.music_library_api.repository.AlbumImageRepository;
import br.gov.mt.seplag.music_library_api.repository.AlbumRepository;
import br.gov.mt.seplag.music_library_api.repository.ArtistaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AlbumService {

    private final AlbumRepository albumRepository;
    private final ArtistaRepository artistaRepository;
    private final AlbumImageRepository albumImageRepository;
    private final MinioStorageService minioStorageService;

    public AlbumService(AlbumRepository albumRepository,
                        ArtistaRepository artistaRepository,
                        AlbumImageRepository albumImageRepository,
                        MinioStorageService minioStorageService) {
        this.albumRepository = albumRepository;
        this.artistaRepository = artistaRepository;
        this.albumImageRepository = albumImageRepository;
        this.minioStorageService = minioStorageService;
    }

    public AlbumResponseDTO criar(AlbumRequestDTO dto) {
        List<Artista> artistas = artistaRepository.findAllById(dto.artistasIds());
        if (artistas.size() != dto.artistasIds().size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Um ou mais IDs de artista não existem");
        }
        Album album = new Album();
        album.setTitulo(dto.titulo());
        album.setArtistas(new HashSet<>(artistas));
        for (Artista a : artistas) {
            a.getAlbuns().add(album);
        }
        album = albumRepository.save(album);
        artistaRepository.saveAll(artistas);
        return toResponse(albumRepository.findWithArtistasById(album.getId()).orElse(album));
    }

    public Page<AlbumResponseDTO> listarPaginado(String nomeArtista, String tipoArtista, Pageable pageable) {
        Page<Album> page;
        if (nomeArtista != null && !nomeArtista.isBlank()) {
            page = albumRepository.findAll(pageable); // filtro por nome pode ser feito em memória ou query
        } else {
            page = albumRepository.findAll(pageable);
        }
        return page.map(this::toResponse);
    }

    public java.util.Optional<AlbumResponseDTO> buscarPorId(Integer id) {
        return albumRepository.findWithArtistasById(id).map(this::toResponse);
    }

    public java.util.Optional<AlbumResponseDTO> atualizar(Integer id, AlbumRequestDTO dto) {
        return albumRepository.findWithArtistasById(id)
                .map(album -> {
                    album.setTitulo(dto.titulo());
                    List<Artista> artistas = artistaRepository.findAllById(dto.artistasIds());
                    if (artistas.size() != dto.artistasIds().size()) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Um ou mais IDs de artista não existem");
                    }
                    Set<Artista> antigos = new HashSet<>(album.getArtistas());
                    for (Artista a : antigos) {
                        a.getAlbuns().remove(album);
                        album.getArtistas().remove(a);
                    }
                    artistaRepository.saveAll(antigos);
                    Set<Artista> novos = new HashSet<>(artistas);
                    album.setArtistas(novos);
                    for (Artista a : novos) {
                        a.getAlbuns().add(album);
                    }
                    artistaRepository.saveAll(novos);
                    return toResponse(albumRepository.save(album));
                });
    }

    public void excluir(Integer id) {
        Album album = albumRepository.findWithArtistasById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Álbum não encontrado"));
        Set<Artista> artistas = new HashSet<>(album.getArtistas());
        for (Artista a : artistas) {
            a.getAlbuns().remove(album);
            album.getArtistas().remove(a);
        }
        artistaRepository.saveAll(artistas);
        albumRepository.delete(album);
    }

    public void uploadCapas(Integer idAlbum, List<MultipartFile> arquivos) {
        Album album = albumRepository.findById(idAlbum)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Álbum não encontrado"));
        for (MultipartFile f : arquivos) {
            String objectKey = minioStorageService.uploadAlbumImage(idAlbum, f);
            AlbumImage img = new AlbumImage();
            img.setAlbum(album);
            img.setObjectKey(objectKey);
            albumImageRepository.save(img);
        }
    }

    public List<String> listarCapasUrls(Integer idAlbum) {
        Album album = albumRepository.findById(idAlbum)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Álbum não encontrado"));
        return albumImageRepository.findByAlbum_Id(idAlbum).stream()
                .map(img -> minioStorageService.presignedGetUrl(img.getObjectKey(), java.time.Duration.ofMinutes(30)))
                .collect(Collectors.toList());
    }

    private AlbumResponseDTO toResponse(Album album) {
        List<ArtistaResumoDTO> artistas = album.getArtistas().stream()
                .map(a -> new ArtistaResumoDTO(a.getId(), a.getNome(), a.getTipoArtista()))
                .toList();
        return new AlbumResponseDTO(album.getId(), album.getTitulo(), artistas);
    }
}
