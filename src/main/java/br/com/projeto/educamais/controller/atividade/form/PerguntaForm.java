package br.com.projeto.educamais.controller.atividade.form;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;

import br.com.projeto.educamais.domain.Alternativa;
import br.com.projeto.educamais.domain.Pergunta;
import lombok.Data;

@Data
public class PerguntaForm {

	@NotNull @NotEmpty @Length(max = 512)
	private String titulo;
	
	@NotNull @NotEmpty @Size(min = 2, max = 5)
	private List<AlternativaForm> alternativas;
	
	public Pergunta getPergunta() {
		List<Alternativa> alternativas = this.alternativas.stream()
			.map(alternativa -> alternativa.getAlternativa())
			.collect(Collectors.toList());
		
		return Pergunta.builder().titulo(titulo).alternativas(alternativas).build();
	}
}
