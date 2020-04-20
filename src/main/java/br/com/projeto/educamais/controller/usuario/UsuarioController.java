package br.com.projeto.educamais.controller.usuario;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.projeto.educamais.controller.generic.form.AlteraNomeForm;
import br.com.projeto.educamais.controller.usuario.form.UsuarioForm;
import br.com.projeto.educamais.domain.Usuario;
import br.com.projeto.educamais.service.interfaces.UsuarioService;
import br.com.projeto.educamais.util.HttpStatusCode;
import br.com.projeto.educamais.util.Util;
import br.com.projeto.educamais.util.messages.UsuarioErrors;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/educamais/usuario")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(value = "Cadastrar uma novo usuário.")
	@ApiResponses({
		@ApiResponse(code = HttpStatusCode.CONFLICT, message = UsuarioErrors.CONFLICT)
	})
	public ResponseEntity<Void> cadastrarUsuario(@RequestBody @Valid UsuarioForm form) {
		usuarioService.salva(form.toUsuario());
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@PutMapping
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation(value = "Alterar o nome do usuário autenticado.")
	public ResponseEntity<Void> alterarNome(@RequestBody @Valid AlteraNomeForm form, Principal principal) {
		
		Usuario usuario = Util.recuperarUsuarioLogado(principal);
		usuario.setNome(form.getNome());		
		usuarioService.atualizarDados(usuario);
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
