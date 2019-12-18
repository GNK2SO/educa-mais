package br.com.projeto.educamais.controller;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.projeto.educamais.controller.dto.ListaTurmaDTO;
import br.com.projeto.educamais.controller.dto.TurmaDTO;
import br.com.projeto.educamais.controller.form.TurmaForm;
import br.com.projeto.educamais.domain.Turma;
import br.com.projeto.educamais.domain.Usuario;
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
		Usuario professor = (Usuario) ((UsernamePasswordAuthenticationToken) pricipal).getPrincipal();
		
		List<Turma> turmas = turmaService.obterTurmasUsuarioAutenticado(professor);
		List<ListaTurmaDTO> turmasDTO = new ListaTurmaDTO().converter(turmas);
		return ResponseEntity.ok(turmasDTO);
	}
	
	@GetMapping("/{id}")
	@Transactional
	public ResponseEntity<TurmaDTO> obterTurma(@PathVariable("id") Long id) {
		Turma turma = turmaService.obterTurmasPorId(id);
		return ResponseEntity.ok(new TurmaDTO(turma));
	}
	
	@PostMapping
	@Transactional
	public ResponseEntity<Turma> cadastrarTurma(@RequestBody @Valid TurmaForm form, Principal pricipal, UriComponentsBuilder uriBuilder) {
		
		//Recuperando usuário logado
		Usuario professor = (Usuario) ((UsernamePasswordAuthenticationToken) pricipal).getPrincipal();
		
		turmaService.salva(form.getTurma(professor));
		URI uri = uriBuilder.build().toUri();
		return ResponseEntity.created(uri).build();
	}
}
