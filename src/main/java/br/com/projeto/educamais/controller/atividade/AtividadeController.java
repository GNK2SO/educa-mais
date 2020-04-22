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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.projeto.educamais.controller.atividade.dto.AtividadeDTO;
import br.com.projeto.educamais.controller.atividade.form.AtividadeForm;
import br.com.projeto.educamais.controller.atividade.form.ListaRespostaForm;
import br.com.projeto.educamais.controller.turma.dto.AlunoTurmaAtividadeDTO;
import br.com.projeto.educamais.controller.turma.dto.ProfessorTurmaAtividadeDTO;
import br.com.projeto.educamais.controller.turma.dto.TurmaAtividadeDTO;
import br.com.projeto.educamais.domain.Atividade;
import br.com.projeto.educamais.domain.Resposta;
import br.com.projeto.educamais.domain.Turma;
import br.com.projeto.educamais.domain.Usuario;
import br.com.projeto.educamais.service.AtividadeService;
import br.com.projeto.educamais.service.interfaces.TurmaService;
import br.com.projeto.educamais.util.HttpStatusCode;
import br.com.projeto.educamais.util.Util;
import br.com.projeto.educamais.util.messages.AtividadeErrors;
import br.com.projeto.educamais.util.messages.TurmaErrors;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/educamais/turmas")
public class AtividadeController {

	@Autowired
	private AtividadeService service;
	
	@Autowired
	private TurmaService turmaService;
	
	@GetMapping("/{turmaId}/atividades")
	@ApiOperation(value = "Obter todas as atividades cadastradas que pertence a turma de id igual à {turmaId}.")
	@ApiResponses({
		@ApiResponse(code = HttpStatusCode.FORBIDDEN, message = TurmaErrors.FORBIDDEN_NOT_PARTICIPATE),
		@ApiResponse(code = HttpStatusCode.NOT_FOUND, message = TurmaErrors.NOT_FOUND)
	})
	public ResponseEntity<TurmaAtividadeDTO> obterTurmaPostagens(@PathVariable Long turmaId, Principal principal) {
		
		Usuario usuario = Util.recuperarUsuarioLogado(principal);
		Turma turma = turmaService.buscarTurmaPorId(turmaId);

		if(turma.professorIsEqualTo(usuario)) {
			List<Atividade> atividades = service.buscarPorTurma(turma, usuario);
			return ResponseEntity.status(HttpStatus.OK).body(new ProfessorTurmaAtividadeDTO(turma, atividades));
		}
		
		List<Atividade> atividades = service.buscarPorTurma(turma, usuario);
		return ResponseEntity.status(HttpStatus.OK).body(new AlunoTurmaAtividadeDTO(turma, atividades));
	}
	
	@PostMapping("{turmaId}/atividades")
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(value = "Cadastrar uma atividade que pertence a turma de id igual à {turmaId}.")
	@ApiResponses({
		@ApiResponse(code = HttpStatusCode.FORBIDDEN, message = AtividadeErrors.FORBIDDEN_SALVAR_ATIVIDADE + "\n" + TurmaErrors.FORBIDDEN_NOT_PARTICIPATE),
		@ApiResponse(code = HttpStatusCode.NOT_FOUND, message = TurmaErrors.NOT_FOUND)
	})
	public ResponseEntity<AtividadeDTO> cadastrarAtividade(@RequestBody @Valid AtividadeForm form,  @PathVariable Long turmaId, Principal principal, UriComponentsBuilder uriBuilder) {
		
		Usuario usuario = Util.recuperarUsuarioLogado(principal);	
		Turma turma = turmaService.buscarTurmaPorId(turmaId);
		Atividade atividade = service.salvar(turma, usuario, form.toAtividade(), form.getIdAlunos());
		
		String path = String.format("/educamais/turmas/%d/atividade/%s", turmaId, atividade.getCodigo());
		URI uri = uriBuilder.path(path).build().toUri();
		
		return ResponseEntity.status(HttpStatus.CREATED).location(uri).body(new AtividadeDTO(atividade));
	}
	
	
	@PostMapping("{turmaId}/atividades/{atividadeId}/respostas")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation(value = "Submeter as repostas da atividade de id igual à {atividadeId} que pertence a turma de id igual à {turmaId}.")
	@ApiResponses({
		@ApiResponse(
			code = HttpStatusCode.FORBIDDEN, 
			message = TurmaErrors.FORBIDDEN_NOT_PARTICIPATE + "\n" + 
					AtividadeErrors.FORBIDDEN_PROFESSOR_SUBMIT_RESPOSTA + "\n" + 
					AtividadeErrors.FORBIDDEN_ATIVIDADE_NOT_PERTENCE_TO_ALUNO + "\n" +
					AtividadeErrors.FORBIDDEN_ATIVIDADE_DESABILITADA + "\n" +
					AtividadeErrors.FORBIDDEN_ATIVIDADE_NOT_PERTENCE_TO_TURMA
		),
		@ApiResponse(code = HttpStatusCode.NOT_FOUND, message = TurmaErrors.NOT_FOUND)
	})
	public ResponseEntity<Void> submeterRespostas(@RequestBody @Valid ListaRespostaForm form, @PathVariable Long turmaId,  @PathVariable Long atividadeId, Principal principal) {
		
		Usuario usuario = Util.recuperarUsuarioLogado(principal);
		Turma turma = turmaService.buscarTurmaPorId(turmaId);
		service.submeterRespostas(Resposta.fromRespostaForm(form.getRespostas()), turma, atividadeId, usuario);
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
}
