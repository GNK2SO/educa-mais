package br.com.projeto.educamais.controller.atividade;

import java.net.URI;
import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.projeto.educamais.controller.atividade.dto.ProfessorAtividadeDTO;
import br.com.projeto.educamais.controller.atividade.form.AtividadeForm;
import br.com.projeto.educamais.controller.turma.dto.AlunoTurmaAtividadeDTO;
import br.com.projeto.educamais.controller.turma.dto.ProfessorTurmaAtividadeDTO;
import br.com.projeto.educamais.controller.turma.dto.TurmaAtividadeDTO;
import br.com.projeto.educamais.domain.Atividade;
import br.com.projeto.educamais.domain.Turma;
import br.com.projeto.educamais.domain.Usuario;
import br.com.projeto.educamais.service.AtividadeService;
import br.com.projeto.educamais.util.Util;

@RestController
@RequestMapping("/educamais/turmas")
public class AtividadeController {

	@Autowired
	private AtividadeService service;
	
	@GetMapping("/{turmaId}/atividades")
	public ResponseEntity<TurmaAtividadeDTO> obterTurmaPostagens(@PathVariable Long turmaId, Principal principal) {
		
		Usuario usuarioLogado = Util.recuperarUsuarioLogado(principal);
		Turma turma = service.buscarTurmaAtividades(turmaId, usuarioLogado);
		
		if(turma.professorIsEqualTo(usuarioLogado)) {
			return ResponseEntity.status(HttpStatus.OK).body(new ProfessorTurmaAtividadeDTO(turma));
		}
		
		List<Atividade> atividadesFiltradas = turma.getAtividadesFiltradasPor(usuarioLogado);
		turma.setAtividades(atividadesFiltradas);
		
		return ResponseEntity.status(HttpStatus.OK).body(new AlunoTurmaAtividadeDTO(turma));
	}
	
	@PostMapping("{turmaId}/atividades")
	public ResponseEntity<ProfessorAtividadeDTO> cadastrarAtividade(@RequestBody @Valid AtividadeForm form,  @PathVariable Long turmaId, Principal principal, UriComponentsBuilder uriBuilder) {
		
		Usuario usuarioLogado = Util.recuperarUsuarioLogado(principal);
		
		Atividade atividade = service.salvar(turmaId, usuarioLogado, form.getAtividade(), form.getIdAlunos());
		
		String path = String.format("/educamais/turmas/%d/atividade/%s", turmaId, atividade.getCodigo());
		
		URI uri = uriBuilder.path(path).build().toUri();
		
		return ResponseEntity.status(HttpStatus.CREATED).location(uri).body(new ProfessorAtividadeDTO(atividade));
	}
}
