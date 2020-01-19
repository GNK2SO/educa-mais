package br.com.projeto.educamais.controller.turma;

import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.created;

import java.net.URI;
import java.security.Principal;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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

import br.com.projeto.educamais.controller.generic.AlteraNomeForm;
import br.com.projeto.educamais.controller.turma.dto.ListaTurmaDTO;
import br.com.projeto.educamais.controller.turma.dto.TurmaDTO;
import br.com.projeto.educamais.controller.turma.form.ParticiparForm;
import br.com.projeto.educamais.controller.turma.form.TurmaForm;
import br.com.projeto.educamais.domain.Turma;
import br.com.projeto.educamais.domain.Usuario;
import br.com.projeto.educamais.exception.UsuarioNaoTemPermissaoParaEssaAtividadeException;
import br.com.projeto.educamais.service.TurmaService;

@RestController
@RequestMapping("/educamais/turma")
public class TurmaController {

	@Autowired
	public TurmaService turmaService;
	
	@GetMapping("/minhasTurmas")
	@Transactional
	public ResponseEntity<List<ListaTurmaDTO>> obterTurmasUsuarioLogado(Principal pricipal) {
		
		//Recuperando usuário logado
		Usuario usuario = (Usuario) ((UsernamePasswordAuthenticationToken) pricipal).getPrincipal();
		
		List<Turma> turmas = turmaService.obterTurmasUsuarioAutenticado(usuario);
		List<ListaTurmaDTO> turmasDTO = new ListaTurmaDTO().converter(turmas);
		return ok(turmasDTO);
	}
	
	@GetMapping("/{id}")
	@Transactional
	public ResponseEntity<TurmaDTO> obterTurma(@PathVariable Long id, Principal pricipal) {
		
		//Recuperando usuário logado
		Usuario usuario = (Usuario) ((UsernamePasswordAuthenticationToken) pricipal).getPrincipal();
		
		Turma turma = turmaService.obterTurmaPorId(id);
		
		
		if(turma.professorIsEqualTo(usuario) || turma.contains(usuario)) {
			return ok(new TurmaDTO(turma));
		}
		
		throw new UsuarioNaoTemPermissaoParaEssaAtividadeException("Usuário não participa turma.");
	}
	
	@PostMapping
	@Transactional
	public ResponseEntity<Turma> cadastrarTurma(@RequestBody @Valid TurmaForm form, Principal pricipal, UriComponentsBuilder uriBuilder) {
		
		//Recuperando usuário logado
		Usuario professor = (Usuario) ((UsernamePasswordAuthenticationToken) pricipal).getPrincipal();
		
		turmaService.salva(form.getTurma(professor));
		URI uri = uriBuilder.build().toUri();
		return created(uri).build();
	}
	
	@PostMapping("/participar")
	@Transactional
	public ResponseEntity<Turma> participarTurma(@RequestBody @Valid ParticiparForm form, Principal pricipal) {
		
		//Recuperando usuário logado
		Usuario usuario = (Usuario) ((UsernamePasswordAuthenticationToken) pricipal).getPrincipal();
		
		Turma turma = turmaService.obterTurmaPorCodigo(form.getCodigoTurma());
		turmaService.participar(turma, usuario);
		
		return ok().build();
	}
	
	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<Turma> alterarNome(@PathVariable Long id, @RequestBody @Valid AlteraNomeForm form, Principal principal) {
		
		//Recuperando usuário logado
		Usuario usuario = (Usuario) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
		
		Turma turma = turmaService.obterTurmaPorId(id);
		turma.setNome(form.getNome());
		
		if(turma.professorIsNotEqualTo(usuario)) {
			throw new UsuarioNaoTemPermissaoParaEssaAtividadeException("Usuário não tem permissão para alterar o nome da turma.");
		}
		
		turmaService.atualizarDados(turma);
		return ok().build();
	}
}
