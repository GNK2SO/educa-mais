package br.com.projeto.educamais.controller.atividade.form;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import br.com.projeto.educamais.domain.Atividade;
import br.com.projeto.educamais.domain.Pergunta;
import lombok.Data;

@Data
public class AtividadeForm {

	@NotNull @NotEmpty @Length(max = 32)
	private String titulo;
	
	@NotNull
	private int tentativas;
	
	@NotNull @NotEmpty
	private List<PerguntaForm> perguntas;
	
	@NotNull @NotEmpty
	private List<Long> idAlunos;
	
	@NotNull @NotEmpty
	private String dataEntrega;
	
	public Atividade getAtividade() {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDate dataEntregaLocalDate = LocalDate.parse(dataEntrega, formatter);
		
		List<Pergunta> perguntas = this.perguntas.stream()
									.map(pergunta -> pergunta.getPergunta())
									.collect(Collectors.toList());
		
		return Atividade.builder()
				.titulo(this.titulo)
				.tentativas(this.tentativas)
				.dataEntrega(dataEntregaLocalDate)
				.perguntas(perguntas)
				.build();
	}

}
