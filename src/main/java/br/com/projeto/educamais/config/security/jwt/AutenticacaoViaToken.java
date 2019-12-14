package br.com.projeto.educamais.config.security.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.projeto.educamais.domain.Usuario;
import br.com.projeto.educamais.service.UsuarioService;

public class AutenticacaoViaToken extends OncePerRequestFilter {
	
	private TokenService tokenService;
	private UsuarioService usuarioService;

	public AutenticacaoViaToken(TokenService tokenService, UsuarioService usuarioService) {
		super();
		this.tokenService = tokenService;
		this.usuarioService = usuarioService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String token = recuperarToken(request);
		boolean isValidToken = tokenService.isValidToken(token);
		if(isValidToken) {
			autenticarUsuario(token);
		}
		filterChain.doFilter(request, response);
	}

	private String recuperarToken(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		if(token == null || token.isEmpty() || !token.startsWith("Bearer ")) {
			return null;
		}
		
		return token.substring(7, token.length());
	}
	
	private void autenticarUsuario(String token) {
		Long idUsuario = tokenService.getIdUsuario(token);
		Usuario usuario = usuarioService.buscarPorId(idUsuario);
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

}
