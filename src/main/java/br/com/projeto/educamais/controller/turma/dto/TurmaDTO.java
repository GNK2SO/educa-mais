package br.com.projeto.educamais.controller.turma.dto;

import java.util.ArrayList;
import java.util.List;

import br.com.projeto.educamais.controller.usuario.dto.UsuarioDTO;
import br.com.projeto.educamais.domain.Turma;

public class TurmaDTO {

	private Long id;
	private String nome;
	private String codigo;
	private UsuarioDTO professor;
	private List<UsuarioDTO> alunos;
	
	public TurmaDTO(Turma turma) {
		List<UsuarioDTO> alunos = new UsuarioDTO().converter(turma.getAlunos());
		
		this.setId(turma.getId());
		this.setNome(turma.getNome());
		this.setCodigo(turma.getCodigo());
		this.setProfessor(new UsuarioDTO(turma.getProfessor()));
		this.setAlunos(alunos);
	}
	
	public List<TurmaDTO> fromTurmas(List<Turma> turmas) {
		
		List<TurmaDTO> turmasDTO = new ArrayList<TurmaDTO>();
		TurmaDTO dto;
		
		for(Turma turma: turmas) {
			dto = new TurmaDTO(turma);
			turmasDTO.add(dto);
		}
		
		return turmasDTO;
	}
	
	//Getters and Setters

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public UsuarioDTO getProfessor() {
		return professor;
	}

	public void setProfessor(UsuarioDTO professor) {
		this.professor = professor;
	}

	public List<UsuarioDTO> getAlunos() {
		return alunos;
	}

	public void setAlunos(List<UsuarioDTO> alunos) {
		this.alunos = alunos;
	}
}
