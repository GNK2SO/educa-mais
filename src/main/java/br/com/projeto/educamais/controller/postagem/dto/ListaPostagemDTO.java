package br.com.projeto.educamais.controller.postagem.dto;

import java.util.List;
import java.util.stream.Collectors;

import br.com.projeto.educamais.domain.Postagem;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ListaPostagemDTO {

	private Long id;
	private String titulo;
	
	public ListaPostagemDTO(Postagem postagem) {
		this.id = postagem.getId();
		this.titulo = postagem.getTitulo();
	}
	
	public List<ListaPostagemDTO> converter(List<Postagem> postagens) {
		return postagens
				.stream()
				.map(postagem -> new ListaPostagemDTO(postagem))
				.collect(Collectors.toList());
	}
}
