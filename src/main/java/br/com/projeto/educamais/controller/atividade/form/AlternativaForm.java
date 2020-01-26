package br.com.projeto.educamais.controller.atividade.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import br.com.projeto.educamais.domain.Alternativa;
import lombok.Data;

@Data
public class AlternativaForm {

	@NotNull @NotEmpty @Length(max = 256)
	private String titulo;
	
	@NotNull
	private boolean correta;
	
	public Alternativa getAlternativa() {
		return Alternativa.builder().titulo(titulo).correta(correta).build();
	}
}
