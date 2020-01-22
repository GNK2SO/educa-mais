package br.com.projeto.educamais.controller.turma;

import java.net.URI;
import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
import br.com.projeto.educamais.controller.turma.dto.TurmaDTO;
import br.com.projeto.educamais.controller.turma.form.ParticiparForm;
import br.com.projeto.educamais.controller.turma.form.TurmaForm;
import br.com.projeto.educamais.domain.Turma;
import br.com.projeto.educamais.domain.Usuario;
import br.com.projeto.educamais.exception.UsuarioNaoTemPermissaoParaEssaAtividadeException;
import br.com.projeto.educamais.service.TurmaService;

@RestController
@RequestMapping("/educamais/turmas")
public class TurmaController {

	@Autowired
	public TurmaService turmaService;
	
	@GetMapping
	public ResponseEntity<List<ListaTurmaDTO>> obterTurmasUsuarioLogado(Principal pricipal) {
		
		//Recuperando usuário logado
		Usuario usuario = (Usuario) ((UsernamePasswordAuthenticationToken) pricipal).getPrincipal();
		
		List<Turma> turmas = turmaService.obterTurmas(usuario);
		List<ListaTurmaDTO> turmasDTO = new ListaTurmaDTO().converter(turmas);
		return ResponseEntity.status(HttpStatus.OK).body(turmasDTO);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<TurmaDTO> obterTurma(@PathVariable Long id, Principal pricipal) {
		
		//Recuperando usuário logado
		Usuario usuario = (Usuario) ((UsernamePasswordAuthenticationToken) pricipal).getPrincipal();
		
		Turma turma = turmaService.obterTurmaPorId(id);
		
		
		if(turma.professorIsEqualTo(usuario) || turma.contains(usuario)) {
			return ResponseEntity.status(HttpStatus.OK).body(new TurmaDTO(turma));
		}
		
		throw new UsuarioNaoTemPermissaoParaEssaAtividadeException("Usuário não participa turma.");
	}
	
	@PostMapping
	public ResponseEntity<Turma> cadastrarTurma(@RequestBody @Valid TurmaForm form, Principal pricipal, UriComponentsBuilder uriBuilder) {
		
		//Recuperando usuário logado
		Usuario professor = (Usuario) ((UsernamePasswordAuthenticationToken) pricipal).getPrincipal();
		
		Turma turma = turmaService.salva(form.getTurma(professor));
		
		URI uri = uriBuilder.path("/educamais/turmas/{id}").buildAndExpand(turma.getId()).toUri();
		
		return ResponseEntity.status(HttpStatus.CREATED).location(uri).build();
	}
	
	@PostMapping("/participar")
	public ResponseEntity<Turma> participarTurma(@RequestBody @Valid ParticiparForm form, Principal pricipal) {
		
		//Recuperando usuário logado
		Usuario usuario = (Usuario) ((UsernamePasswordAuthenticationToken) pricipal).getPrincipal();
		
		Turma turma = turmaService.obterTurmaPorCodigo(form.getCodigoTurma());
		turmaService.participar(turma, usuario);
		
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	@PostMapping("/{id}/sair")
	public ResponseEntity<Turma> sairTurma(@PathVariable Long id, Principal pricipal) {
		
		//Recuperando usuário logado
		Usuario usuario = (Usuario) ((UsernamePasswordAuthenticationToken) pricipal).getPrincipal();
		
		Turma turma = turmaService.obterTurmaPorId(id);
		turmaService.sairTurma(turma, usuario);
		
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Turma> alterarNome(@PathVariable Long id, @RequestBody @Valid AlteraNomeForm form, Principal principal) {
		
		//Recuperando usuário logado
		Usuario usuario = (Usuario) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
		
		Turma turma = turmaService.obterTurmaPorId(id);
		turma.setNome(form.getNome());
		
		if(turma.professorIsNotEqualTo(usuario)) {
			throw new UsuarioNaoTemPermissaoParaEssaAtividadeException("Usuário não tem permissão para alterar o nome da turma.");
		}
		
		turmaService.atualizarDados(turma);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
