package br.com.projeto.educamais.service;

import static org.mockito.Mockito.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.projeto.educamais.domain.Alternativa;
import br.com.projeto.educamais.domain.Pergunta;
import br.com.projeto.educamais.repository.PerguntaRepository;
import br.com.projeto.educamais.service.interfaces.PerguntaService;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class PerguntaServiceTest {

	@MockBean
	private PerguntaRepository repository;
	
	@Autowired
	private PerguntaService service;

	@Test
	public void listaPerguntaComNPerguntasDeveChamarMetodoSalvarNVezes()
	{
		
		Alternativa alternativa_1 = Alternativa.builder()
									.id(1L)
									.titulo("Alternativa 1")
									.correta(true)
									.build();
		
		Alternativa alternativa_2 = Alternativa.builder()
									.id(2L)
									.titulo("Alternativa 2")
									.correta(false)
									.build();
		
		List<Alternativa> alternativas = Arrays.asList(alternativa_1, alternativa_2);
		
		Pergunta pergunta_1 = Pergunta.builder()
								.id(1L)
								.titulo("pergunta 1")
								.nota(10.0)
								.alternativas(alternativas)
								.build();
	
		Pergunta pergunta_2 = Pergunta.builder()
								.id(2L)
								.titulo("pergunta 2")
								.nota(10.0)
								.alternativas(alternativas)
								.build();
		
		Pergunta pergunta_3 = Pergunta.builder()
								.id(3L)
								.titulo("pergunta 3")
								.nota(10.0)
								.alternativas(alternativas)
								.build();
		
		List<Pergunta> perguntas = Arrays.asList(pergunta_1, pergunta_2, pergunta_3);
		
		
		assertDoesNotThrow(() -> {
			service.salvar(perguntas);
		});
		
		verify(repository, times(perguntas.size())).saveAndFlush(any(Pergunta.class));
	}
}
