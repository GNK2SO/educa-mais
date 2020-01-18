package br.com.projeto.educamais.controller.turma.form;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParticiparForm {

	@NotNull @NotEmpty @Length(min = 8, max = 8)
	String codigoTurma;
}
