package br.com.projeto.educamais.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.projeto.educamais.domain.Usuario;
import br.com.projeto.educamais.service.interfaces.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class JwtServiceTest {

	@Autowired
	private JwtService service;
	
	@Value("${jwt.secret}")
	private String secret;
	
	@Value("${jwt.expiration}")
	private String expiration;
	
	@Test
	public void dadosTokenCriadoDevemSerIntegros()
	{
		Long usuarioId = 1L;
		
		Usuario usuario = new Usuario();
		usuario.setId(usuarioId);
		
		String token = service.gerarToken(usuario);
		
		Claims body = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
		
		assertEquals(usuarioId.toString(), body.getSubject());
	}
	
	@Test
	public void tokenValidoDeveRetornarTrue()
	{
		Long usuarioId = 1L;
		
		Usuario usuario = new Usuario();
		usuario.setId(usuarioId);
		
		Date hoje = new Date();
		Date dataExpiracao = new Date(hoje.getTime() + Long.parseLong(expiration));
		
		String token =  Jwts.builder()
					.setIssuer("Teste API Educa+")
					.setSubject(usuario.getId().toString())
					.setIssuedAt(hoje)
					.setExpiration(dataExpiracao)
					.signWith(SignatureAlgorithm.HS256, secret)
					.compact();
		
		assertTrue(service.isValidToken(token));
	}
	
	@Test
	public void tokenInvalidoDeveRetornarFalse()
	{
		String token = "String qualquer coisa";
		assertFalse(service.isValidToken(token));
	}
	
	@Test
	public void tokenExpiradoDeveRetornarFalse()
	{
		Long usuarioId = 1L;
		
		Usuario usuario = new Usuario();
		usuario.setId(usuarioId);
		
		Date hoje = new Date();
		Date dataExpiracao = new Date(hoje.getTime() - Long.parseLong(expiration));
		
		String token =  Jwts.builder()
					.setIssuer("Teste API Educa+")
					.setSubject(usuario.getId().toString())
					.setIssuedAt(hoje)
					.setExpiration(dataExpiracao)
					.signWith(SignatureAlgorithm.HS256, secret)
					.compact();
		
		assertFalse(service.isValidToken(token));
	}
	
	@Test
	public void deveRetornaIdUsuario()
	{
		Long usuarioId = 1L;
		
		Usuario usuario = new Usuario();
		usuario.setId(usuarioId);
		
		Date hoje = new Date();
		Date dataExpiracao = new Date(hoje.getTime() + Long.parseLong(expiration));
		
		String token =  Jwts.builder()
					.setIssuer("Teste API Educa+")
					.setSubject(usuario.getId().toString())
					.setIssuedAt(hoje)
					.setExpiration(dataExpiracao)
					.signWith(SignatureAlgorithm.HS256, secret)
					.compact();
		
		assertEquals(service.getIdUsuario(token), usuarioId);
	}
}
