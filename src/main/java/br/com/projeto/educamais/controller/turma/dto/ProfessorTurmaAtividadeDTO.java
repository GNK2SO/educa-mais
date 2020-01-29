package br.com.projeto.educamais.controller.turma.dto;

import java.util.ArrayList;
import java.util.List;

import br.com.projeto.educamais.controller.atividade.dto.ProfessorAtividadeDTO;
import br.com.projeto.educamais.controller.usuario.dto.UsuarioDTO;
import br.com.projeto.educamais.domain.Turma;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ProfessorTurmaAtividadeDTO extends TurmaAtividadeDTO {

	private List<UsuarioDTO> alunos;
	private List<ProfessorAtividadeDTO> atividades;
	
	public ProfessorTurmaAtividadeDTO(Turma turma) {
		super(turma);
		List<UsuarioDTO> alunos = new UsuarioDTO().converter(turma.getAlunos());
		List<ProfessorAtividadeDTO> atividades = new ProfessorAtividadeDTO().converter(turma.getAtividades());
		this.setAlunos(alunos);
		this.setAtividades(atividades);
	}
	
	@Override
	public List<TurmaDTO> fromTurmas(List<Turma> turmas) {
		
		List<TurmaDTO> turmasDTO = new ArrayList<TurmaDTO>();
		TurmaDTO dto;
		
		for(Turma turma: turmas) {
			dto = new TurmaDTO(turma);
			turmasDTO.add(dto);
		}
		
		return turmasDTO;
	}
}