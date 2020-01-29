package br.com.projeto.educamais.controller.turma.dto;

import java.util.List;

import br.com.projeto.educamais.controller.usuario.dto.UsuarioDTO;
import br.com.projeto.educamais.domain.Turma;
import lombok.Data;

@Data
public class ParticipantesTurmaDTO {


	private Long id;
	private String nome;
	private String codigo;
	private UsuarioDTO professor;
	private List<UsuarioDTO> alunos;
	
	public ParticipantesTurmaDTO(Turma turma) {
		List<UsuarioDTO> alunos = new UsuarioDTO().converter(turma.getAlunos());
		
		this.setId(turma.getId());
		this.setNome(turma.getNome());
		this.setCodigo(turma.getCodigo());
		this.setProfessor(new UsuarioDTO(turma.getProfessor()));
		this.setAlunos(alunos);
	}
}
