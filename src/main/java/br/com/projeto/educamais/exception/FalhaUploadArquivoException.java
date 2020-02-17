package br.com.projeto.educamais.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class FalhaUploadArquivoException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public static final String message = "Falha ao salvar arquivo.";
	
	public FalhaUploadArquivoException() {
		super(message);
	}
}
