package br.com.projeto.educamais.service.interfaces;

import java.util.List;
import br.com.projeto.educamais.domain.Usuario;

public interface UsuarioService {

	public List<Usuario> ObterTodosUsuarios();
	public Usuario buscarPorId(Long idUsuario);
	public Usuario buscarPorEmail(String email);
	public void salva(Usuario usuario);
	public void atualizarDados(Usuario usuario);
}
