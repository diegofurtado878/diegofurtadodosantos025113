package br.gov.mt.seplag.music_library_api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "ALBUM_CAPA")
public class AlbumCapa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CAPA")
    private Integer id;

    @JsonIgnore
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ALBUM", nullable = false)
    private Album album;

    @Column(name = "NOME_ARQUIVO", nullable = false)
    private String nomeArquivo;

    @Column(name = "OBJETO_MINIO", nullable = false, unique = true, length = 512)
    private String objetoMinio;

    public Integer getId() { return id; }
    public Album getAlbum() { return album; }
    public void setAlbum(Album album) { this.album = album; }
    public String getNomeArquivo() { return nomeArquivo; }
    public void setNomeArquivo(String nomeArquivo) { this.nomeArquivo = nomeArquivo; }
    public String getObjetoMinio() { return objetoMinio; }
    public void setObjetoMinio(String objetoMinio) { this.objetoMinio = objetoMinio; }
}
