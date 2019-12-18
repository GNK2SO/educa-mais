package br.com.projeto.educamais.controller.dto;

public class ErroFormularioDTO {

	private String campo;
	private String erro;
	
	public ErroFormularioDTO(String campo, String erro) {
		this.campo = campo;
		this.erro = erro;
	}
	
	public String getCampo() {
		return campo;
	}
	public void setCampo(String campo) {
		this.campo = campo;
	}
	public String getErro() {
		return erro;
	}
	public void setErro(String erro) {
		this.erro = erro;
	}
}
