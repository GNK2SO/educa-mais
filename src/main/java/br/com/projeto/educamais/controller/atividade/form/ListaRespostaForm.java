package br.com.projeto.educamais.controller.atividade.form;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class ListaRespostaForm {
	
	@NotNull @NotEmpty
	private List<RespostaForm> respostas;
}
