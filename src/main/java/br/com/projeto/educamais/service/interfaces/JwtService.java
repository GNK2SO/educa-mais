package br.com.projeto.educamais.service.interfaces;


import br.com.projeto.educamais.domain.Usuario;

public interface JwtService {

	public String gerarToken(Usuario usuario);
	public boolean isValidToken(String token);
	public Long getIdUsuario(String token);
}
