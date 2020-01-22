package br.com.projeto.educamais.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class EntidadeInexistenteException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public EntidadeInexistenteException(String mensagem) {
		super(mensagem);
	}

}
