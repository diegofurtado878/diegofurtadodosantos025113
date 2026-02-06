package br.gov.mt.seplag.music_library_api.repository;

import br.gov.mt.seplag.music_library_api.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    UserDetails findByLogin(String login);
}
