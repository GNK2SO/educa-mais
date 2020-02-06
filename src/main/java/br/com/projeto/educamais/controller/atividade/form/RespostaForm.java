package br.com.projeto.educamais.controller.atividade.form;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class RespostaForm {

	@NotNull
	public Long perguntaId;
	
	@NotNull
	public Long alternativaId;
}
