package br.com.projeto.educamais.controller.postagem.dto;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import br.com.projeto.educamais.domain.Arquivo;
import br.com.projeto.educamais.domain.Postagem;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostagemDTO {

	private Long id;
	
	private String titulo;
	
	private String descricao;
	
	private String dataPublicacao;
	
	private List<Arquivo> arquivos;
	
	public PostagemDTO(Postagem postagem) {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		
		this.id = postagem.getId();
		this.titulo = postagem.getTitulo();
		this.descricao = postagem.getDescricao();
		this.dataPublicacao = postagem.getDataCriacao().format(formatter);
		this.arquivos = postagem.getArquivos();
	}
	
	public List<PostagemDTO> converter(List<Postagem> postagens) {
		return postagens
				.stream()
				.map(postagem -> new PostagemDTO(postagem))
				.collect(Collectors.toList());
	}
}
