package br.com.projeto.educamais.controller.usuario;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.projeto.educamais.controller.generic.form.AlteraNomeForm;
import br.com.projeto.educamais.controller.usuario.dto.UsuarioDTO;
import br.com.projeto.educamais.controller.usuario.form.UsuarioForm;
import br.com.projeto.educamais.domain.Usuario;
import br.com.projeto.educamais.service.UsuarioService;
import br.com.projeto.educamais.util.Util;

@RestController
@RequestMapping("/educamais/usuario")
public class UsuarioController {

	@Autowired
	public UsuarioService usuarioService;
	
	@GetMapping
	public ResponseEntity<List<UsuarioDTO>> obterTodosUsuarios() {
		List<Usuario> usuarios = usuarioService.ObterTodosUsuarios();
		List<UsuarioDTO> listaUsuariosDTO =  new UsuarioDTO().converter(usuarios);
		return ResponseEntity.status(HttpStatus.OK).body(listaUsuariosDTO);
	}
	
	@PostMapping
	public ResponseEntity<Usuario> cadastrarUsuario(@RequestBody @Valid UsuarioForm form) {
		usuarioService.salva(form.getUsuario());
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@PutMapping
	public ResponseEntity<Usuario> alterarNome(@RequestBody @Valid AlteraNomeForm form, Principal principal) {
		
		Usuario usuarioLogado = Util.recuperarUsuarioLogado(principal);
		usuarioLogado.setNome(form.getNome());
		
		usuarioService.atualizarDados(usuarioLogado);
		
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
