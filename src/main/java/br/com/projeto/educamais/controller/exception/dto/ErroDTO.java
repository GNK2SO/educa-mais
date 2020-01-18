package br.com.projeto.educamais.controller.exception.dto;

public class ErroDTO {

	private int status;
	private String erro;
	private String mensagem;
	
	public ErroDTO(String erro, int status, String mensagem) {
		super();
		this.erro = erro;
		this.status = status;
		this.mensagem = mensagem;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getErro() {
		return erro;
	}

	public void setErro(String erro) {
		this.erro = erro;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

}
