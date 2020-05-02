package br.com.projeto.educamais.repository;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.projeto.educamais.domain.Atividade;
import br.com.projeto.educamais.domain.Turma;
import br.com.projeto.educamais.domain.Usuario;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ExtendWith(SpringExtension.class)
public class AtividadeRepositoryTest {

	@Autowired
	public AtividadeRepository repository;
	
	@Autowired
	public TestEntityManager entityManager;
	
	@Test
	public void deveRetornarListaAtividadePertencenteTurmaPreenchida() 
	{
		Turma turma = new Turma();
		turma.setNome("Turma Teste");
		turma.setCodigo("39BAC6EF");
		
		Atividade atividade_1 = new Atividade();
		atividade_1.setTurma(turma);
		
		Atividade atividade_2 = new Atividade();
		atividade_2.setTurma(turma);
		
		entityManager.persist(turma);
		entityManager.persist(atividade_1);
		entityManager.persist(atividade_2);
		
		List<Atividade> atividadesObtidas = repository.findAllByTurma(turma);
		
		assertTrue(atividadesObtidas.containsAll(Arrays.asList(atividade_1, atividade_2)));
	}
	
	@Test
	public void deveRetornarListaAtividadePertencenteTurmaVazia() 
	{
		Turma turma = new Turma();
		turma.setNome("Turma Teste");
		turma.setCodigo("39BAC6EF");
		
		entityManager.persist(turma);
		
		List<Atividade> atividadesObtidas = repository.findAllByTurma(turma);
		
		assertTrue(atividadesObtidas.isEmpty());
	}

	@Test
	public void deveRetornarListaAtividadePertencenteAlunoPreenchida() 
	{
		Usuario usuario = new Usuario();
		usuario.setNome("usuario");
		usuario.setEmail("usuario@email.com");
		
		Atividade atividade_1 = new Atividade();
		atividade_1.setAluno(usuario);
		
		Atividade atividade_2 = new Atividade();
		atividade_2.setAluno(usuario);
		
		entityManager.persist(usuario);
		entityManager.persist(atividade_1);
		entityManager.persist(atividade_2);
		
		List<Atividade> atividadesObtidas = repository.findAllByAluno(usuario);
		
		assertTrue(atividadesObtidas.containsAll(Arrays.asList(atividade_1, atividade_2)));
	}
	
	@Test
	public void deveRetornarListaAtividadePertencenteAlunoVazia() 
	{
		Usuario usuario = new Usuario();
		usuario.setNome("usuario");
		usuario.setEmail("usuario@email.com");
		
		entityManager.persist(usuario);
		
		List<Atividade> atividadesObtidas = repository.findAllByAluno(usuario);
		
		assertTrue(atividadesObtidas.isEmpty());
	}
}

