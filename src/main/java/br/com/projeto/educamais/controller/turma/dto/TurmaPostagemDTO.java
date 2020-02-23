package br.com.projeto.educamais.controller.turma.dto;

import java.util.List;

import br.com.projeto.educamais.controller.postagem.dto.PostagemDTO;
import br.com.projeto.educamais.controller.usuario.dto.UsuarioDTO;
import br.com.projeto.educamais.domain.Postagem;
import br.com.projeto.educamais.domain.Turma;
import lombok.Data;

@Data
public class TurmaPostagemDTO {
	private Long id;
	private String nomeTurma;
	private String codigoTurma;
	private UsuarioDTO professor;
	private List<PostagemDTO> postagens;
	
	public TurmaPostagemDTO(Turma turma, List<Postagem> postagens) {
		List<PostagemDTO> postagensDTO = new PostagemDTO().converter(postagens);
		
		this.setId(turma.getId());
		this.setNomeTurma(turma.getNome());
		this.setCodigoTurma(turma.getCodigo());
		this.setProfessor(new UsuarioDTO(turma.getProfessor()));
		this.setPostagens(postagensDTO);
	}
}
