package br.com.projeto.educamais.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class UsuarioExistenteException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public UsuarioExistenteException() {
		super("Falha ao inserir usuário. Já existe um usuário cadastrado com esse e-mail.");
	}

}
