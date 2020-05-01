package br.com.projeto.educamais.controller.authentication;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.projeto.educamais.controller.authentication.dto.LoginDTO;
import br.com.projeto.educamais.controller.authentication.form.LoginForm;
import br.com.projeto.educamais.domain.Usuario;
import br.com.projeto.educamais.exception.EntidadeInexistenteException;
import br.com.projeto.educamais.service.interfaces.JwtService;
import br.com.projeto.educamais.util.HttpStatusCode;
import br.com.projeto.educamais.util.messages.AutenticacaoErrors;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/auth")
public class AutenticacaoController {
	
	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private JwtService tokenService;

	@PostMapping
	@ApiOperation(value = "Autenticação da conta de um usuário.")
	@ApiResponses({
		@ApiResponse(code = HttpStatusCode.NOT_FOUND, message = AutenticacaoErrors.NOT_FOUND)
	})
	public ResponseEntity<LoginDTO> autenticar(@RequestBody @Valid LoginForm form) {
		
		UsernamePasswordAuthenticationToken dadosLogin = form.toUsuario();
		
		try {
			Authentication authentication = authManager.authenticate(dadosLogin);
			Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
			String token = tokenService.gerarToken(usuarioLogado);
			return ResponseEntity.ok(new LoginDTO(token));
		} catch(AuthenticationException e) {
			throw new EntidadeInexistenteException(AutenticacaoErrors.NOT_FOUND);
		}
	}
}
