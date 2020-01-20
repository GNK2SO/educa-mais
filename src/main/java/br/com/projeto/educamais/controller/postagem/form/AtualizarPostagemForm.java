package br.com.projeto.educamais.controller.postagem.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
public class AtualizarPostagemForm {
	
	@NotEmpty @NotNull @Length(max = 32)
	private String titulo;
	
	@NotEmpty @NotNull @Length(max = 512)
	private String descricao;
}
