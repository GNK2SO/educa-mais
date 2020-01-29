package br.com.projeto.educamais.controller.atividade.dto;

import java.util.List;
import java.util.stream.Collectors;

import br.com.projeto.educamais.domain.Pergunta;
import lombok.Data;

@Data
public class PerguntaDTO {
	
	private Long id;
	
	private String titulo;
	
	private double nota;
	
	List<AlternativaDTO> alternativas;
	
	public PerguntaDTO(Pergunta pergunta) {
		
		List<AlternativaDTO> alternativas = pergunta.getAlternativas()
				.stream().map(alternativa -> new AlternativaDTO(alternativa))
				.collect(Collectors.toList());
		
		this.id = pergunta.getId();
		this.titulo = pergunta.getTitulo();
		this.nota = pergunta.getNota();
		this.alternativas = alternativas;
	}
}
