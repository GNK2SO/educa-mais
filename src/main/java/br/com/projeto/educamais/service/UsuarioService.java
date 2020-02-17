package br.com.projeto.educamais.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.projeto.educamais.domain.Usuario;
import br.com.projeto.educamais.exception.EntidadeExistenteException;
import br.com.projeto.educamais.exception.EntidadeInexistenteException;
import br.com.projeto.educamais.repository.UsuarioRepository;
import br.com.projeto.educamais.util.messages.UsuarioErrors;

@Service
public class UsuarioService extends GenericService {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Transactional
	public List<Usuario> ObterTodosUsuarios() {
		return usuarioRepository.findAll();
	}
	
	@Transactional
	public void salva(Usuario usuario) {
		
		if(usuarioRepository.existsByEmail(usuario.getEmail())) {
			throw new EntidadeExistenteException(UsuarioErrors.CONFLICT);
		}
		
		preencherCamposAuditoria(usuario, usuario);
		usuarioRepository.save(usuario);
	}

	@Transactional
	public Usuario buscarPorId(Long idUsuario) {
		 Optional<Usuario> usuario = usuarioRepository.findById(idUsuario);
		 if(usuario.isPresent()) {
			 return usuario.get();
		 }
		 throw new EntidadeInexistenteException(UsuarioErrors.NOT_FOUND);
	}
	
	@Transactional
	public Usuario buscarPorEmail(String email) {
		 Usuario usuario = usuarioRepository.findByEmail(email);
		 
		 if(usuario != null) {
			 return usuario;
		 }
		 
		 throw new EntidadeInexistenteException(UsuarioErrors.NOT_FOUND);
	}
	
	@Transactional
	public void atualizarDados(Usuario usuario) {
		preencherCamposAuditoria(usuario, usuario);
	}
}
