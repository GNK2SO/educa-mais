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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.projeto.educamais.controller.generic.form.AlteraNomeForm;
import br.com.projeto.educamais.controller.turma.dto.ListaTurmaDTO;
import br.com.projeto.educamais.controller.turma.dto.ParticipantesTurmaDTO;
import br.com.projeto.educamais.controller.turma.form.ParticiparForm;
import br.com.projeto.educamais.controller.turma.form.TurmaForm;
import br.com.projeto.educamais.domain.Arquivo;
import br.com.projeto.educamais.domain.Turma;
import br.com.projeto.educamais.domain.Usuario;
import br.com.projeto.educamais.exception.UsuarioNaoTemPermissaoParaEssaAtividadeException;
import br.com.projeto.educamais.service.TurmaService;
import br.com.projeto.educamais.util.Storage;
import br.com.projeto.educamais.util.Util;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/educamais/turmas")
public class TurmaController {

	@Autowired
	private TurmaService turmaService;
	
	@Autowired
	private Storage storage;
	
	@GetMapping
	@ApiOperation(value = "Obter todas as turmas relacionadas ao usuário autenticado, seja como professor ou aluno.")
	public ResponseEntity<List<ListaTurmaDTO>> obterTurmasUsuarioLogado(Principal principal) {
		
		Usuario usuarioLogado = Util.recuperarUsuarioLogado(principal);
		
		List<Turma> turmas = turmaService.buscarTurmas(usuarioLogado);
		List<ListaTurmaDTO> turmasDTO = new ListaTurmaDTO().converter(turmas);
		return ResponseEntity.status(HttpStatus.OK).body(turmasDTO);
	}
	
	@GetMapping("/{turmaId}/participantes")
	@ApiOperation(value = "Obter todos os alunos da turma de id igual à {turmaId}.")
	public ResponseEntity<ParticipantesTurmaDTO> obterParticipantes(@PathVariable Long turmaId, Principal principal) {
		
		Usuario usuarioLogado = Util.recuperarUsuarioLogado(principal);
		
		Turma turma = turmaService.buscarTurmaPorId(turmaId);
		
		if(turma.professorIsEqualTo(usuarioLogado) || turma.contains(usuarioLogado)) {
			return ResponseEntity.status(HttpStatus.OK).body(new ParticipantesTurmaDTO(turma));
		}
		
		throw new UsuarioNaoTemPermissaoParaEssaAtividadeException("Usuário não participa turma.");
	}
	
	@PostMapping
	@ApiOperation(value = "Cadastrar uma nova turma e relacionar o usuário autenticado como professor.")
	public ResponseEntity<Turma> cadastrarTurma(@RequestBody @Valid TurmaForm form, Principal principal, UriComponentsBuilder uriBuilder) {
		
		Usuario professor = Util.recuperarUsuarioLogado(principal);
		
		Turma turma = turmaService.salva(form.getTurma(professor));
		
		URI uri = uriBuilder.path("/educamais/turmas/{id}").buildAndExpand(turma.getId()).toUri();
		
		return ResponseEntity.status(HttpStatus.CREATED).location(uri).build();
	}
	
	@PostMapping("/participar")
	@ApiOperation(value = "Relacionar o usuário autenticado a turma de id igual à {turmaId} como aluno.")
	public ResponseEntity<Turma> participarTurma(@RequestBody @Valid ParticiparForm form, Principal principal) {
		
		Usuario usuarioLogado = Util.recuperarUsuarioLogado(principal);
		
		Turma turma = turmaService.buscarTurmaPorCodigo(form.getCodigoTurma());
		turmaService.participar(turma, usuarioLogado);
		
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	@PostMapping("/{turmaId}/sair")
	@ApiOperation(value = "Remover relação entre usuário autenticado e turma de id igual à {turmaId}.")
	public ResponseEntity<Turma> sairTurma(@PathVariable Long turmaId, Principal principal) {
		
		Usuario usuarioLogado = Util.recuperarUsuarioLogado(principal);
		
		Turma turma = turmaService.buscarTurmaPorId(turmaId);
		turmaService.sairTurma(turma, usuarioLogado);
		
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	@PutMapping("/{turmaId}")
	@ApiOperation(value = "Atualizar dados da turma de id igual à {turmaId}.")
	public ResponseEntity<Turma> alterarNome(@PathVariable Long turmaId, @RequestBody @Valid AlteraNomeForm form, Principal principal) {
		
		Usuario usuarioLogado = Util.recuperarUsuarioLogado(principal);
		
		Turma turma = turmaService.buscarTurmaPorId(turmaId);
		turma.setNome(form.getNome());
		
		if(turma.professorIsNotEqualTo(usuarioLogado)) {
			throw new UsuarioNaoTemPermissaoParaEssaAtividadeException("Usuário não tem permissão para alterar o nome da turma.");
		}
		
		turmaService.atualizarDados(turma);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	@DeleteMapping("/{turmaId}")
	@ApiOperation(value = "Remover turma de id igual à {turmaId}.")
	public ResponseEntity<Turma> deletarTurma(@PathVariable Long turmaId, Principal principal) {
		
		Usuario usuarioLogado = Util.recuperarUsuarioLogado(principal);
		
		Turma turma = turmaService.buscarTurmaPorId(turmaId);
		
		if(turma.professorIsNotEqualTo(usuarioLogado)) {
			throw new UsuarioNaoTemPermissaoParaEssaAtividadeException("Usuário não tem permissão para deletar turma.");
		}
		
		List<Arquivo> arquivos = turmaService.deletar(turma);
		
		arquivos.stream().forEach(arquivo->{
			storage.deletar(arquivo);
		});
		
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
