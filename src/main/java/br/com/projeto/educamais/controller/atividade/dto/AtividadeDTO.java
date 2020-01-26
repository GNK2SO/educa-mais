package br.com.projeto.educamais.controller.atividade.dto;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import br.com.projeto.educamais.domain.Atividade;
import br.com.projeto.educamais.domain.Pergunta;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AtividadeDTO {

	private String titulo;
	
	private String codigo;
	
	private int tentativas;
	
	private boolean habilitada;
	
	private String dataEntrega;
	
	private List<Pergunta> perguntas;
	
	public AtividadeDTO(Atividade atividade) {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		
		this.titulo = atividade.getTitulo();
		this.codigo = atividade.getCodigo();
		this.tentativas = atividade.getTentativas();
		this.habilitada = atividade.estaHabilitada();
		this.dataEntrega = atividade.getDataEntrega().format(formatter);
		this.perguntas = atividade.getPerguntas();
	}

	public List<AtividadeDTO> converter(List<Atividade> atividades) {
		return atividades.stream()
				.map(atividade -> new AtividadeDTO(atividade))
				.collect(Collectors.toList());
	}

}
