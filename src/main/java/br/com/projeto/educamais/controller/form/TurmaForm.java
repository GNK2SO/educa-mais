package br.com.projeto.educamais.controller.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import br.com.projeto.educamais.domain.Turma;
import br.com.projeto.educamais.domain.Usuario;

public class TurmaForm {

	@NotNull @NotEmpty @Length(max = 32)
	String nomeTurma;

	public String getNomeTurma() {
		return nomeTurma;
	}

	public void setNomeTurma(String nomeTurma) {
		this.nomeTurma = nomeTurma;
	}
	
	public Turma getTurma(Usuario professor) {
		Turma turma = new Turma();
		turma.setNome(this.nomeTurma);
		turma.setProfessor(professor);
		return turma;
	}
}
