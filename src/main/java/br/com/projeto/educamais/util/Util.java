package br.com.projeto.educamais.util;

import java.security.Principal;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.com.projeto.educamais.domain.Usuario;

public class Util {

	public static String criptografar(String texto)
	{
		return new BCryptPasswordEncoder().encode(texto);
	}
	
	public static Usuario recuperarUsuarioLogado(Principal principal) {
		return (Usuario) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
	}
}
