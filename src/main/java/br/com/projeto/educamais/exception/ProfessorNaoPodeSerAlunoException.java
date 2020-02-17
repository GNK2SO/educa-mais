package br.com.projeto.educamais.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class ProfessorNaoPodeSerAlunoException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public static final String message = "Acesso Negado. Usuário não pode ser professor e aluno da mesma turma.";
	
	public ProfessorNaoPodeSerAlunoException() {
		super(message);
	}
}
