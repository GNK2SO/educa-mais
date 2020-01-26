package br.com.projeto.educamais.controller.turma.dto;

import java.util.ArrayList;
import java.util.List;

import br.com.projeto.educamais.controller.atividade.dto.AtividadeDTO;
import br.com.projeto.educamais.controller.postagem.dto.PostagemDTO;
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
	private List<PostagemDTO> postagens;
	private List<AtividadeDTO> atividades;
	
	public TurmaDTO(Turma turma) {
		List<UsuarioDTO> alunos = new UsuarioDTO().converter(turma.getAlunos());
		List<PostagemDTO> postagens = new PostagemDTO().converter(turma.getPostagens());
		List<AtividadeDTO> atividades = new AtividadeDTO().converter(turma.getAtividades());
		
		this.setId(turma.getId());
		this.setNome(turma.getNome());
		this.setCodigo(turma.getCodigo());
		this.setProfessor(new UsuarioDTO(turma.getProfessor()));
		this.setAlunos(alunos);
		this.setPostagens(postagens);
		this.setAtividades(atividades);
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
