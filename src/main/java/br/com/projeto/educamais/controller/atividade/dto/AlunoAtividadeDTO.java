package br.com.projeto.educamais.controller.atividade.dto;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import br.com.projeto.educamais.domain.Atividade;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AlunoAtividadeDTO  {

	private String titulo;
	
	private String codigo;
	
	private int tentativas;
	
	private boolean habilitada;
	
	private String dataEntrega;
	
	private List<PerguntaDTO> perguntas;
	
	public AlunoAtividadeDTO(Atividade atividade) {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		
		List<PerguntaDTO> perguntas = atividade.getPerguntas()
				.stream().map(pergunta -> new PerguntaDTO(pergunta))
				.collect(Collectors.toList());
		
		this.titulo = atividade.getTitulo();
		this.codigo = atividade.getCodigo();
		this.tentativas = atividade.getTentativas();
		this.habilitada = atividade.estaHabilitada();
		this.dataEntrega = atividade.getDataEntrega().format(formatter);
		this.perguntas = perguntas;
	}

	public List<AlunoAtividadeDTO> converter(List<Atividade> atividades) {
		return atividades.stream()
				.map(atividade -> new AlunoAtividadeDTO(atividade))
				.collect(Collectors.toList());
	}

}
