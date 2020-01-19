package br.com.projeto.educamais.controller.postagem.dto;

import br.com.projeto.educamais.domain.Postagem;
import lombok.Data;

@Data
public class PostagemDTO {

	private Long id;
	
	private String titulo;
	
	private String descricao;
	
	private String dataPublicacao;
	
	public PostagemDTO(Postagem postagem) {
		this.id = postagem.getId();
		this.titulo = postagem.getTitulo();
		this.descricao = postagem.getDescricao();
		this.dataPublicacao = postagem.getDataCriacao().toString();
	}
	
}
