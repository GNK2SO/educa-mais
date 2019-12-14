package br.com.projeto.educamais.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.projeto.educamais.domain.Usuario;
import br.com.projeto.educamais.exception.UsuarioExistenteException;
import br.com.projeto.educamais.repository.UsuarioRepository;

@Service
public class UsuarioService extends GenericService {

	@Autowired
	public UsuarioRepository usuarioRepository;
	
	@Transactional
	public List<Usuario> ObterTodosUsuarios() {
		return usuarioRepository.findAll();
	}
	
	@Transactional
	public void salva(Usuario usuario) {
		
		if(usuarioRepository.findByEmail(usuario.getEmail()) != null) {
			throw new UsuarioExistenteException();
		}
		
		preencherCamposAuditoria(usuario);
		usuarioRepository.save(usuario);
	}

	public Usuario buscarPorId(Long idUsuario) {
		 Optional<Usuario> usuario = usuarioRepository.findById(idUsuario);
		 if(usuario.isPresent()) {
			 return usuario.get();
		 }
		 return null;
	}
}
