package br.com.projeto.educamais.controller.turma.dto;

import java.util.ArrayList;
import java.util.List;

import br.com.projeto.educamais.controller.postagem.dto.ListaPostagemDTO;
import br.com.projeto.educamais.controller.usuario.dto.UsuarioDTO;
import br.com.projeto.educamais.domain.Turma;
import lombok.Data;
@Data
public class TurmaDTO {

	private Long id;
	private String nome;
	private String codigo;
	private UsuarioDTO professor;
	private List<UsuarioDTO> alunos;
	private List<ListaPostagemDTO> postagens;
	
	public TurmaDTO(Turma turma) {
		List<UsuarioDTO> alunos = new UsuarioDTO().converter(turma.getAlunos());
		List<ListaPostagemDTO> postagens = new ListaPostagemDTO().converter(turma.getPostagens());
		
		this.setId(turma.getId());
		this.setNome(turma.getNome());
		this.setCodigo(turma.getCodigo());
		this.setProfessor(new UsuarioDTO(turma.getProfessor()));
		this.setAlunos(alunos);
		this.setPostagens(postagens);
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
}
