package br.com.projeto.educamais.controller.atividade.dto;

import br.com.projeto.educamais.domain.Alternativa;
import lombok.Data;

@Data
public class AlternativaDTO {
	private Long id;
	private String titulo;
	
	public AlternativaDTO(Alternativa alternativa) {
		this.id = alternativa.getId();
		this.titulo = alternativa.getTitulo();
	}
}
