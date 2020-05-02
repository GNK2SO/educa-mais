package br.com.projeto.educamais.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.projeto.educamais.domain.Usuario;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ExtendWith(SpringExtension.class)
public class UsuarioRepositoryTest {
	
	@Autowired
	private UsuarioRepository repository;
	
	@Autowired
	private TestEntityManager entityManager;
	
	private final String EMAIL_USUARIO = "usuario@email.com";

	@Test
	public void deveRetornarTrueQuandoVerificarExistenciaEmail()
	{
		Usuario usuario = Usuario.builder()
							.nome("Usuario")
							.email(EMAIL_USUARIO)
							.build();
		
		entityManager.persist(usuario);
		
		boolean existByEmail = repository.existsByEmail(EMAIL_USUARIO);
		assertTrue(existByEmail);
	}
	
	@Test
	public void deveRetornarFalseQuandoVerificarExistenciaEmail()
	{
		boolean existByEmail = repository.existsByEmail(EMAIL_USUARIO);
		assertFalse(existByEmail);
	}
	
	@Test
	public void deveRetornarOptinalPreenchidoQuandoBuscarEmail()
	{
		Usuario usuario = Usuario.builder()
							.nome("Usuario")
							.email(EMAIL_USUARIO)
							.build();
		
		entityManager.persist(usuario);
		
		Optional<Usuario> usuarioOptional = repository.findByEmail(EMAIL_USUARIO);
		
		assertTrue(usuarioOptional.isPresent());
		
		Usuario usuarioObtido = usuarioOptional.get();
		
		assertEquals(usuario.getNome(), usuarioObtido.getNome());
		assertEquals(usuario.getEmail(), usuarioObtido.getEmail());
	}
	
	@Test
	public void deveRetornarOptinalVazioQuandoBuscarEmail()
	{
		Optional<Usuario> usuario = repository.findByEmail(EMAIL_USUARIO);
		assertFalse(usuario.isPresent());
	}
}
