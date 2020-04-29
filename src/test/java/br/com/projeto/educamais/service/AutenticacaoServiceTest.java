package br.com.projeto.educamais.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.projeto.educamais.domain.Usuario;
import br.com.projeto.educamais.repository.UsuarioRepository;
import br.com.projeto.educamais.service.interfaces.AutenticacaoService;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AutenticacaoServiceTest {

	@MockBean
	private UsuarioRepository repository;
	
	@Autowired
	private AutenticacaoService service;
	
	@Test
	public void emailInexistenteDeveLevantarException()
	{
		String emailInexistente = "email_inexistente@email.com";
		
		when(repository.findByEmail(emailInexistente)).thenReturn(Optional.empty());
		
		assertThrows(UsernameNotFoundException.class, () -> {
			service.loadUserByUsername(emailInexistente);
		});
	}
	
	@Test
	public void emailExistenteNaoDeveLevantarException()
	{

		String emailExistente = "email_existente@email.com";
		
		Usuario usuario = Usuario.builder()
					.nome("Usuario")
					.email(emailExistente)
					.build();
		
		
		when(repository.findByEmail(emailExistente)).thenReturn(Optional.of(usuario));
		
		UserDetails usuarioObtido = service.loadUserByUsername(emailExistente);
		
		assertTrue(usuario.equals(usuarioObtido));
	}
}
