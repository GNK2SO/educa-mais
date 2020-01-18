package br.com.projeto.educamais.controller.turma.dto;

import java.util.ArrayList;
import java.util.List;

import br.com.projeto.educamais.domain.Turma;

public class ListaTurmaDTO {

	private Long id;
	private String nome;
	
	public ListaTurmaDTO() {}
	
	public ListaTurmaDTO(Turma turma) {
		this.setId(turma.getId());
		this.setNome(turma.getNome());
	}
	
	public List<ListaTurmaDTO> converter(List<Turma> turmas) {
		
		List<ListaTurmaDTO> turmasDTO = new ArrayList<ListaTurmaDTO>();
		ListaTurmaDTO dto;
		
		for(Turma turma: turmas) {
			dto = new ListaTurmaDTO(turma);
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
}
