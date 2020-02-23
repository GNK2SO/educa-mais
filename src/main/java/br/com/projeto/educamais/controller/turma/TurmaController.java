package br.com.projeto.educamais.controller.turma;

import java.net.URI;
import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.projeto.educamais.controller.generic.form.AlteraNomeForm;
import br.com.projeto.educamais.controller.turma.dto.ListaTurmaDTO;
import br.com.projeto.educamais.controller.turma.dto.ParticipantesTurmaDTO;
import br.com.projeto.educamais.controller.turma.form.ParticiparForm;
import br.com.projeto.educamais.controller.turma.form.TurmaForm;
import br.com.projeto.educamais.domain.Turma;
import br.com.projeto.educamais.domain.Usuario;
import br.com.projeto.educamais.exception.ProfessorNaoPodeSerAlunoException;
import br.com.projeto.educamais.exception.UsuarioJaEstaNaTurmaException;
import br.com.projeto.educamais.exception.UsuarioNaoTemPermissaoParaEssaAtividadeException;
import br.com.projeto.educamais.service.TurmaService;
import br.com.projeto.educamais.util.HttpStatusCode;
import br.com.projeto.educamais.util.Util;
import br.com.projeto.educamais.util.messages.TurmaErrors;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/educamais/turmas")
public class TurmaController {

	@Autowired
	private TurmaService turmaService;
	
	@GetMapping
	@ApiOperation(value = "Obter todas as turmas relacionadas ao usuário autenticado, seja como professor ou aluno.")
	public ResponseEntity<List<ListaTurmaDTO>> obterTurmasUsuarioLogado(Principal principal) {
		
		Usuario usuario = Util.recuperarUsuarioLogado(principal);
		List<Turma> turmas = turmaService.buscarTurmas(usuario);
		List<ListaTurmaDTO> turmasDTO = new ListaTurmaDTO().converter(turmas);
		
		return ResponseEntity.status(HttpStatus.OK).body(turmasDTO);
	}
	
	@GetMapping("/{turmaId}/participantes")
	@ApiOperation(value = "Obter todos os alunos da turma de id igual à {turmaId}.")
	@ApiResponses({
		@ApiResponse(code = HttpStatusCode.FORBIDDEN, message = TurmaErrors.FORBIDDEN_NOT_PARTICIPATE),
		@ApiResponse(code = HttpStatusCode.NOT_FOUND, message = TurmaErrors.NOT_FOUND)
	})
	public ResponseEntity<ParticipantesTurmaDTO> obterParticipantes(@PathVariable Long turmaId, Principal principal) {
		
		Usuario usuario = Util.recuperarUsuarioLogado(principal);
		Turma turma = turmaService.buscarTurmaPorId(turmaId);
		
		if(turma.professorIsEqualTo(usuario) || turma.contains(usuario)) {
			return ResponseEntity.status(HttpStatus.OK).body(new ParticipantesTurmaDTO(turma));
		}
		
		throw new UsuarioNaoTemPermissaoParaEssaAtividadeException(TurmaErrors.FORBIDDEN_NOT_PARTICIPATE);
	}
	
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(value = "Cadastrar uma nova turma e relacionar o usuário autenticado como professor.")
	@ApiResponses({
		@ApiResponse(code = HttpStatusCode.CONFLICT, message = TurmaErrors.CONFLICT)
	})
	public ResponseEntity<Void> cadastrarTurma(@RequestBody @Valid TurmaForm form, Principal principal, UriComponentsBuilder uriBuilder) {
		
		Usuario professor = Util.recuperarUsuarioLogado(principal);
		Turma turma = turmaService.salva(form.getTurma(professor));
		URI uri = uriBuilder.path("/educamais/turmas/{id}").buildAndExpand(turma.getId()).toUri();
		
		return ResponseEntity.status(HttpStatus.CREATED).location(uri).build();
	}
	
	
	@PostMapping("/participar")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation(value = "Relacionar o usuário autenticado a turma de id igual à {turmaId} como aluno.")
	@ApiResponses({
		@ApiResponse(code = HttpStatusCode.FORBIDDEN, message = ProfessorNaoPodeSerAlunoException.message + "\n" + UsuarioJaEstaNaTurmaException.message),
		@ApiResponse(code = HttpStatusCode.NOT_FOUND, message = TurmaErrors.NOT_FOUND)
	})
	public ResponseEntity<Void> participarTurma(@RequestBody @Valid ParticiparForm form, Principal principal) {
		
		Usuario usuario = Util.recuperarUsuarioLogado(principal);
		Turma turma = turmaService.buscarTurmaPorCodigo(form.getCodigoTurma());
		turmaService.participar(turma, usuario);
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
	
	@PostMapping("/{turmaId}/sair")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation(value = "Remover relação entre usuário autenticado e turma de id igual à {turmaId}.")
	@ApiResponses({
		@ApiResponse(code = HttpStatusCode.FORBIDDEN, message = TurmaErrors.FORBIDDEN_SAIR_TURMA),
		@ApiResponse(code = HttpStatusCode.NOT_FOUND, message = TurmaErrors.NOT_FOUND)
	})
	public ResponseEntity<Void> sairTurma(@PathVariable Long turmaId, Principal principal) {
		
		Usuario usuario = Util.recuperarUsuarioLogado(principal);
		Turma turma = turmaService.buscarTurmaPorId(turmaId);
		turmaService.sairTurma(turma, usuario);
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
	
	@PutMapping("/{turmaId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation(value = "Atualizar dados da turma de id igual à {turmaId}.")
	@ApiResponses({
		@ApiResponse(code = HttpStatusCode.FORBIDDEN, message = TurmaErrors.FORBIDDEN_ATUALIZAR_TURMA),
		@ApiResponse(code = HttpStatusCode.NOT_FOUND, message = TurmaErrors.NOT_FOUND)
	})
	public ResponseEntity<Void> alterarNome(@PathVariable Long turmaId, @RequestBody @Valid AlteraNomeForm form, Principal principal) {
		
		Usuario usuario = Util.recuperarUsuarioLogado(principal);
		turmaService.alterarNome(turmaId, form.getNome(), usuario);
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
	
	@DeleteMapping("/{turmaId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation(value = "Remover turma de id igual à {turmaId}.")
	@ApiResponses({
		@ApiResponse(code = HttpStatusCode.FORBIDDEN, message = TurmaErrors.FORBIDDEN_REMOVER_TURMA),
		@ApiResponse(code = HttpStatusCode.NOT_FOUND, message = TurmaErrors.NOT_FOUND)
	})
	public ResponseEntity<Void> deletarTurma(@PathVariable Long turmaId, Principal principal) {
		
		Usuario usuario = Util.recuperarUsuarioLogado(principal);
		turmaService.deletar(turmaId, usuario);
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
