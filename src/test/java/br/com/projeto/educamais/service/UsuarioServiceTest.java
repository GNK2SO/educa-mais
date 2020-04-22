package br.com.projeto.educamais.service;


import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static br.com.projeto.educamais.util.Util.criptografar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import br.com.projeto.educamais.domain.Usuario;
import br.com.projeto.educamais.exception.EntidadeExistenteException;
import br.com.projeto.educamais.exception.EntidadeInexistenteException;
import br.com.projeto.educamais.repository.UsuarioRepository;
import br.com.projeto.educamais.service.interfaces.UsuarioService;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UsuarioServiceTest {
	
	private final Long ID = 1L;
	private final String NOME = "usuario";
	private final String EMAIL = "usuario@email.com";
	private final String SENHA = "12345678";
	

	@MockBean
	private UsuarioRepository repository;
	
	@Autowired
	private UsuarioService service;
	
	private Usuario usuario;
	
	@BeforeEach
	public void setUp()
	{
		String senhaCriptografada = criptografar(SENHA);
		
		usuario =  Usuario.builder()
					.nome(NOME)
					.email(EMAIL)
					.senha(senhaCriptografada)
					.build();
	}
	
	
	@Test
	public void salvarDeveChamarSaveDoRepositorio()
	{
		service.salva(usuario);
		verify(repository).save(any(Usuario.class));
	}
	
	@Test
	public void naoDeveSalvarUsuarioComEmailExistente()
	{
		when(repository.existsByEmail(EMAIL)).thenReturn(true);
		
		assertThrows(EntidadeExistenteException.class, () -> {
			service.salva(usuario);
	    });
	}
	
	@Test
	public void deveRetornarTodosUsuarioCadastrados()
	{
		Usuario usuario_1 = Usuario.builder()
				.nome("usuario_1")
				.build();
		
		Usuario usuario_2 = Usuario.builder()
				.nome("usuario_2")
				.build();
		
		Usuario usuario_3 = Usuario.builder()
				.nome("usuario_3")
				.build();
								
		List<Usuario> usuarios = new ArrayList<Usuario>();
		usuarios.add(usuario_1);
		usuarios.add(usuario_2);
		usuarios.add(usuario_3);
		
		when(repository.findAll()).thenReturn(usuarios);
		
		List<Usuario> usuariosEncontrados = service.ObterTodosUsuarios();
		
		assertEquals(usuariosEncontrados.size(), usuarios.size());
		assertTrue(usuariosEncontrados.contains(usuario_1));
		assertTrue(usuariosEncontrados.contains(usuario_2));
		assertTrue(usuariosEncontrados.contains(usuario_3));
	}
	
	@Test
	public void idValidoDeveRetornaUsuario()
	{
		when(repository.findById(ID)).thenReturn(Optional.of(usuario));
		Usuario usuarioEncontrado = service.buscarPorId(ID);
		assertTrue(usuarioEncontrado.equals(usuario));
	}

	@Test
	public void idInvalidoDeveRetornaException()
	{
		when(repository.findById(ID)).thenReturn(Optional.empty());
		assertThrows(EntidadeInexistenteException.class, () -> {
			service.buscarPorId(ID);
	    });
	}
	
	@Test
	public void emailValidoDeveRetornaUsuario()
	{
		when(repository.findByEmail(EMAIL)).thenReturn(Optional.of(usuario));
		Usuario usuarioEncontrado = service.buscarPorEmail(EMAIL);
		assertTrue(usuarioEncontrado.equals(usuario));
	}

	@Test
	public void emailInvalidoDeveRetornaException()
	{
		when(repository.findByEmail(EMAIL)).thenReturn(Optional.empty());
		assertThrows(EntidadeInexistenteException.class, () -> {
			service.buscarPorEmail(EMAIL);
	    });
	}
	
	@Test
	public void deveAtualizarEmailUsuario()
	{
		usuario.setNome("NOVO_NOME");
		usuario.setEmail("NOVO_EMAIL@EMAIL.COM");
		
		service.atualizarDados(usuario);
		
		verify(repository).save(any(Usuario.class));
	}
}
