package br.com.projeto.educamais.controller.postagem.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import br.com.projeto.educamais.domain.Postagem;
import lombok.Data;

@Data
public class AtualizarPostagemForm {
	
	@NotEmpty @NotNull @Length(max = 32)
	private String titulo;
	
	@NotEmpty @NotNull @Length(max = 512)
	private String descricao;

	public Postagem getPostagem(Long postagemId) {
		Postagem postagem = new Postagem();
		postagem.setId(postagemId);
		postagem.setTitulo(titulo);
		postagem.setDescricao(descricao);
		return postagem;
	}
}
