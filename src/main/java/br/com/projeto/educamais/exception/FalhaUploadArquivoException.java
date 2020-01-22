package br.com.projeto.educamais.exception;

public class FalhaUploadArquivoException extends RuntimeException {

private static final long serialVersionUID = 1L;
	
	public FalhaUploadArquivoException() {
		super("Falha ao salvar arquivo.");
	}
}
