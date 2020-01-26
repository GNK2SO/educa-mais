package br.com.projeto.educamais.controller.postagem.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import br.com.projeto.educamais.domain.Postagem;
import lombok.Data;

@Data
public class PostagemForm {
	
	@NotEmpty @NotNull @Length(max = 32)
	private String titulo;
	
	@NotEmpty @NotNull @Length(max = 512)
	private String descricao;
	
	public Postagem getPostagem() {
		return Postagem.builder().titulo(this.titulo).descricao(this.descricao).build();
	}
}
