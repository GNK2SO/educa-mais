package br.com.projeto.educamais.controller.postagem;

import static org.springframework.http.ResponseEntity.created;

import java.net.URI;
import java.security.Principal;

import javax.transaction.Transactional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.projeto.educamais.controller.postagem.form.PostagemForm;
import br.com.projeto.educamais.domain.Postagem;
import br.com.projeto.educamais.domain.Usuario;
import br.com.projeto.educamais.service.PostagemService;

@RestController
@RequestMapping("/educamais/postagens")
public class PostagemController {

	@Autowired
	public PostagemService postagemService;
	
	@PostMapping
	@Transactional
	public ResponseEntity<Postagem> cadastrarPostagem(@RequestBody @Valid PostagemForm form, Principal principal, UriComponentsBuilder uriBuilder) {
		
		//Recuperando usuário logado
		Usuario usuario = (Usuario) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
		
		postagemService.salvar(form.getIdTurma(), usuario, form.getPostagem());
		URI uri = uriBuilder.build().toUri();
		return created(uri).build();
	}
}
