package br.com.projeto.educamais.controller.atividade.dto;

import java.time.format.DateTimeFormatter;
import java.util.List;

import br.com.projeto.educamais.domain.Atividade;
import br.com.projeto.educamais.domain.Pergunta;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AtividadeDTO {
	private Long id;
	
	private String titulo;
	
	private String codigo;
	
	private double nota;
	
	private int tentativas;
	
	private boolean habilitada;
	
	private String dataEntrega;
	
	private List<Pergunta> perguntas;
	
	public AtividadeDTO(Atividade atividade) {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		
		this.id = atividade.getId();
		this.titulo = atividade.getTitulo();
		this.codigo = atividade.getCodigo();
		this.nota = atividade.getNota();
		this.tentativas = atividade.getTentativas();
		this.habilitada = atividade.estaHabilitada();
		this.dataEntrega = atividade.getDataEntrega().format(formatter);
		this.perguntas = atividade.getPerguntas();
	}
}
