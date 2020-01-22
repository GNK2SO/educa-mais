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

import br.com.projeto.educamais.config.security.jwt.TokenService;
import br.com.projeto.educamais.controller.authentication.dto.LoginDTO;
import br.com.projeto.educamais.controller.authentication.form.LoginForm;
import br.com.projeto.educamais.exception.EntidadeInexistenteException;

@RestController
@RequestMapping("/auth")
public class AutenticacaoController {
	
	@Autowired
	public AuthenticationManager authManager;
	
	@Autowired
	public TokenService tokenService;

	@PostMapping
	public ResponseEntity<LoginDTO> autenticar(@RequestBody @Valid LoginForm form) {
		
		UsernamePasswordAuthenticationToken dadosLogin = form.getUsuario();
		
		try {
			Authentication authentication = authManager.authenticate(dadosLogin);
			String token = tokenService.gerarToken(authentication);
			return ResponseEntity.ok(new LoginDTO(token));
		} catch(AuthenticationException e) {
			throw new EntidadeInexistenteException("Falha ao autenticar. Usuário não está cadastrado.");
		}
	}
}
