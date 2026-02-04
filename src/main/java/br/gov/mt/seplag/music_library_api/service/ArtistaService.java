package br.gov.mt.seplag.music_library_api.service;

import br.gov.mt.seplag.music_library_api.entity.Artista;
import br.gov.mt.seplag.music_library_api.repository.ArtistaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ArtistaService {

    @Autowired
    private ArtistaRepository repository;

    public List<Artista> buscarTodos() { // Nome que o controller espera
        return repository.findAll();
    }

    public Optional<Artista> buscarPorId(Integer id) {
        return repository.findById(id);
    }

    public Artista salvar(Artista artista) {
        return repository.save(artista);
    }
}