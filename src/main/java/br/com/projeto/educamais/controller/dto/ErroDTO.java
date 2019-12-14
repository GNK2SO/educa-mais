package br.com.projeto.educamais.controller.dto;

public class ErroDTO {


	public int status;
	public String erro;
	public String mensagem;
	public String path;
	
	public ErroDTO(String erro, int status, String mensagem, String path) {
		super();
		this.erro = erro;
		this.status = status;
		this.mensagem = mensagem;
		this.path = path;
	}
}
