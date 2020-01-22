package br.com.projeto.educamais.controller.postagem;

import java.security.Principal;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.projeto.educamais.controller.postagem.form.AtualizarPostagemForm;
import br.com.projeto.educamais.controller.postagem.form.PostagemForm;
import br.com.projeto.educamais.domain.Postagem;
import br.com.projeto.educamais.domain.Usuario;
import br.com.projeto.educamais.service.PostagemService;
import br.com.projeto.educamais.util.Util;

@RestController
@RequestMapping("/educamais/turmas")
public class PostagemController {

	@Autowired
	public PostagemService postagemService;
	
	@PostMapping("/{id}/postagem")
	public ResponseEntity<Postagem> cadastrarPostagem(@RequestBody @Valid PostagemForm form, @PathVariable("id") Long idTurma, Principal principal) {
		
		Usuario usuarioLogado = Util.recuperarUsuarioLogado(principal);
		
		Postagem postagem = postagemService.salvar(idTurma, usuarioLogado, form.getPostagem());
		
		return ResponseEntity.status(HttpStatus.CREATED).body(postagem);
	}
	
	@PutMapping("/{turmaId}/postagem/{postagemId}")
	public ResponseEntity<Postagem> atualizarPostagem(@RequestBody @Valid AtualizarPostagemForm form, @PathVariable Long turmaId, @PathVariable Long postagemId, Principal principal) {
		
		Usuario usuarioLogado = Util.recuperarUsuarioLogado(principal);
		
		postagemService.atualizarPostagem(turmaId, usuarioLogado, postagemId, form);
		
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	@DeleteMapping("/{turmaId}/postagem/{postagemId}")
	public ResponseEntity<Postagem> deletarPostagem(@PathVariable Long turmaId, @PathVariable Long postagemId, Principal principal) {
		
		Usuario usuarioLogado = Util.recuperarUsuarioLogado(principal);
		
		postagemService.deletarPostagem(turmaId, usuarioLogado, postagemId);
		
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
