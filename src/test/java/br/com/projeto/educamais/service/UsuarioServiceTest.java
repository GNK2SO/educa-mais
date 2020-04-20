package br.com.projeto.educamais.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static br.com.projeto.educamais.util.Util.criptografar;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import br.com.projeto.educamais.domain.Usuario;
import br.com.projeto.educamais.exception.EntidadeExistenteException;
import br.com.projeto.educamais.exception.EntidadeInexistenteException;
import br.com.projeto.educamais.repository.UsuarioRepository;
import br.com.projeto.educamais.service.interfaces.UsuarioService;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
public class UsuarioServiceTest {
	
	private final String NOME = "usuario";
	private final String EMAIL = "usuario@email.com";
	private final String SENHA = "12345678";
	

	@Autowired
	private UsuarioRepository repository;
	
	@Autowired
	private UsuarioService service;
	
	@Test
	@Order(1)
	public void naoDeveSalvarUsuarioComNomeMaiorQue16Caracteres()
	{
		assertThrows(RuntimeException.class, () -> {
			
			String senhaCriptografada = criptografar(SENHA);
			
			Usuario usuario = new Usuario();
			usuario.setNome("NOME MUITO GRANDE COM MAIS DE 16 CARACTERES");
			usuario.setEmail(EMAIL);
			usuario.setSenha(senhaCriptografada);
			
			service.salva(usuario);
	    });
	}
	
	@Test
	@Order(2)
	public void naoDeveSalvarUsuarioComEmailMaiorQue32Caracteres()
	{
		assertThrows(RuntimeException.class, () -> {
			
			String senhaCriptografada = criptografar(SENHA);
			
			Usuario usuario = new Usuario();
			usuario.setNome(NOME);
			usuario.setEmail("EMAIL MUITO GRANDE COM MAIS DE 32 CARACTERES");
			usuario.setSenha(senhaCriptografada);
			
			service.salva(usuario);
	    });
	}
	
	@Test
	@Order(3)
	public void salvarDeveChamarSaveDoRepositorio()
	{
		String senhaCriptografada = criptografar(SENHA);
		
		Usuario usuario = new Usuario();
		usuario.setNome(NOME);
		usuario.setEmail(EMAIL);
		usuario.setSenha(senhaCriptografada);
		
		assertFalse(repository.existsByEmail(usuario.getEmail()));
		repository.save(usuario);
		assertTrue(repository.existsByEmail(usuario.getEmail()));
	}
	
	@Test
	@Order(4)
	public void naoDeveSalvarUsuarioComEmailExistente()
	{
		assertThrows(EntidadeExistenteException.class, () -> {
			
			String senhaCriptografada = criptografar(SENHA);
			
			Usuario usuario = new Usuario();
			usuario.setNome(NOME);
			usuario.setEmail(EMAIL);
			usuario.setSenha(senhaCriptografada);
			
			service.salva(usuario);
	    });
	}
	
	@Test
	@Order(5)
	public void deveRetornarTodosUsuarioCadastrados()
	{
		List<Usuario> usuarios = service.ObterTodosUsuarios();
		assertEquals(usuarios.size(), 1);
	}
	
	@Test
	@Order(6)
	public void idValidoDeveRetornaUsuario()
	{
		assertDoesNotThrow(() -> {
			List<Usuario> usuarios = service.ObterTodosUsuarios();
			service.buscarPorId(usuarios.get(0).getId());
		});
	}

	@Test
	@Order(7)
	public void idInvalidoDeveRetornaException()
	{
		assertThrows(EntidadeInexistenteException.class, () -> {
			service.buscarPorId(2L);
	    });
	}
	
	@Test
	@Order(8)
	public void emailValidoDeveRetornaUsuario()
	{
		assertDoesNotThrow(() -> {
			service.buscarPorEmail(EMAIL);
		});
	}

	@Test
	@Order(9)
	public void emailInvalidoDeveRetornaException()
	{
		assertThrows(EntidadeInexistenteException.class, () -> {
			service.buscarPorEmail("USUARIO_INEXISTENTE@EMAIL.COM");
	    });
	}
	
	@Test
	@Order(10)
	public void nomeMaior16CaractereRetornaException()
	{
	
		Usuario usuario;
		final String NOVO_NOME = "NOME MUITO GRANDE COM MAIS DE 16 CARACTERES";
		
		usuario = service.buscarPorEmail(EMAIL);
		
		System.out.println(usuario.getNome());
		
		assertTrue(usuario.getNome().equals(NOME));
		assertFalse(usuario.getNome().equals(NOVO_NOME));
		
		assertThrows(RuntimeException.class, () -> {
			usuario.setNome(NOVO_NOME);
			service.atualizarDados(usuario);
	    });
	}
	
	@Test
	@Order(11)
	public void deveAtualizarNomeUsuario()
	{
		Usuario usuario;
		final String NOVO_NOME = "NOVO_NOME";
		
		usuario = service.buscarPorEmail(EMAIL);
		assertTrue(usuario.getNome().equals(NOME));
		assertFalse(usuario.getNome().equals(NOVO_NOME));
		
		usuario.setNome(NOVO_NOME);
		service.atualizarDados(usuario);
		
		assertFalse(usuario.getNome().equals(NOME));
		assertTrue(usuario.getNome().equals(NOVO_NOME));
	}
	
	@Test
	@Order(12)
	public void emailMaior16CaractereRetornaException()
	{
	
		Usuario usuario;
		final String NOVO_EMAIL = "EMAIL MUITO GRANDE COM MAIS DE 32 CARACTERES";
		
		usuario = service.buscarPorEmail(EMAIL);
		
		System.out.println(usuario.getNome());
		
		assertTrue(usuario.getEmail().equals(EMAIL));
		assertFalse(usuario.getEmail().equals(NOVO_EMAIL));
		
		assertThrows(RuntimeException.class, () -> {
			usuario.setEmail(NOVO_EMAIL);
			service.atualizarDados(usuario);
	    });
	}
	
	@Test
	@Order(13)
	public void deveAtualizarEmailUsuario()
	{
		Usuario usuario;
		final String NOVO_EMAIL = "NOVO_EMAIL@EMAIL.COM";
		
		usuario = service.buscarPorEmail(EMAIL);
		assertTrue(usuario.getEmail().equals(EMAIL));
		assertFalse(usuario.getEmail().equals(NOVO_EMAIL));
		
		usuario.setEmail(NOVO_EMAIL);
		service.atualizarDados(usuario);
		
		assertFalse(usuario.getEmail().equals(EMAIL));
		assertTrue(usuario.getEmail().equals(NOVO_EMAIL));
	}
}
