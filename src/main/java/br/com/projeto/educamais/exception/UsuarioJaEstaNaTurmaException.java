package br.com.projeto.educamais.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class UsuarioJaEstaNaTurmaException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public UsuarioJaEstaNaTurmaException() {
		super("Acesso Negado. Usuário já está participando da turma.");
	}
}
