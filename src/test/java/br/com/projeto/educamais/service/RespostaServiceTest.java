package br.com.projeto.educamais.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.projeto.educamais.domain.Resposta;
import br.com.projeto.educamais.repository.RespostaRepository;
import br.com.projeto.educamais.service.interfaces.RespostaService;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class RespostaServiceTest {

	@MockBean
	private RespostaRepository repository;
	
	@Autowired
	private RespostaService service;

	@Test
	public void listaRespostaComNRespostasDeveChamarMetodoSalvarNVezes()
	{	
		Resposta resposta_1 = new Resposta();
		resposta_1.setAlternativaId(1L);
		resposta_1.setPerguntaId(1L);
		
		Resposta resposta_2 = new Resposta();
		resposta_2.setAlternativaId(1L);
		resposta_2.setPerguntaId(1L);
		
		Resposta resposta_3 = new Resposta();
		resposta_3.setAlternativaId(2L);
		resposta_3.setPerguntaId(1L);
		
		
		List<Resposta> respostas = Arrays.asList(resposta_1, resposta_2, resposta_3);
		
		assertDoesNotThrow(() -> {
			service.salvar(respostas);
		});
		
		verify(repository, times(respostas.size())).saveAndFlush(any(Resposta.class));
	}
}
