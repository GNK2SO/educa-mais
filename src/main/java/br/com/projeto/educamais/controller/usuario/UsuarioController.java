package br.com.projeto.educamais.controller.usuario;

import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.created;

import java.net.URI;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.projeto.educamais.controller.usuario.dto.UsuarioDTO;
import br.com.projeto.educamais.controller.usuario.form.UsuarioForm;
import br.com.projeto.educamais.domain.Usuario;
import br.com.projeto.educamais.service.UsuarioService;

@RestController
@RequestMapping("/educamais/usuario")
public class UsuarioController {

	@Autowired
	public UsuarioService usuarioService;
	
	@GetMapping
	public ResponseEntity<List<UsuarioDTO>> obterTodosUsuarios() {
		List<Usuario> usuarios = usuarioService.ObterTodosUsuarios();
		List<UsuarioDTO> listaUsuariosDTO =  new UsuarioDTO().converter(usuarios);
		return ok(listaUsuariosDTO);
	}
	
	@PostMapping
	@Transactional
	public ResponseEntity<Usuario> cadastrarUsuario(@RequestBody @Valid UsuarioForm form, UriComponentsBuilder uriBuilder) {
		usuarioService.salva(form.getUsuario());
		URI uri = uriBuilder.build().toUri();
		return created(uri).build();
	}
}
