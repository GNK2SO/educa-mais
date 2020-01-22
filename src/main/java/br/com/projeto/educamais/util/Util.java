package br.com.projeto.educamais.util;

import java.security.Principal;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import br.com.projeto.educamais.domain.Usuario;

public class Util {

	public static Usuario recuperarUsuarioLogado(Principal principal) {
		return (Usuario) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
	}
}
