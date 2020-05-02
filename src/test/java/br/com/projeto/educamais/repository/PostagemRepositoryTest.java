package br.com.projeto.educamais.repository;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.projeto.educamais.domain.Postagem;
import br.com.projeto.educamais.domain.Turma;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ExtendWith(SpringExtension.class)
public class PostagemRepositoryTest {

	@Autowired
	private PostagemRepository repository;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void deveRetornarListaPostagemPertencenteTurma()
	{
		Turma turma = new Turma();
		turma.setNome("Turma Teste");
		turma.setCodigo("39BAC6EF");
		
		Postagem postagem_1 = new Postagem();
		postagem_1.setTurma(turma);
		
		Postagem postagem_2 = new Postagem();
		postagem_2.setTurma(turma);
		
		Postagem postagem_3 = new Postagem();
		postagem_3.setTurma(turma);
		
		entityManager.persist(turma);
		entityManager.persist(postagem_1);
		entityManager.persist(postagem_2);
		entityManager.persist(postagem_3);
		
		List<Postagem> turmasObtidas = repository.findAllByTurma(turma);
		
		assertTrue(turmasObtidas.containsAll(Arrays.asList(postagem_1, postagem_2, postagem_3)));
	}
	
	@Test
	public void deveRetornarListaPostagemVazia()
	{
		Turma turma = new Turma();
		turma.setNome("Turma Teste");
		turma.setCodigo("39BAC6EF");
		
		entityManager.persist(turma);
		
		List<Postagem> turmasObtidas = repository.findAllByTurma(turma);
		
		assertTrue(turmasObtidas.isEmpty());
	}
	
	@Test
	public void deveRetornarPageListaPostagemPertencenteTurma()
	{
		Turma turma = new Turma();
		turma.setNome("Turma Teste");
		turma.setCodigo("39BAC6EF");
		
		Postagem postagem_1 = new Postagem();
		postagem_1.setTurma(turma);
		
		Postagem postagem_2 = new Postagem();
		postagem_2.setTurma(turma);
		
		Postagem postagem_3 = new Postagem();
		postagem_3.setTurma(turma);
		
		entityManager.persist(turma);
		entityManager.persist(postagem_1);
		entityManager.persist(postagem_2);
		entityManager.persist(postagem_3);
		
		Pageable page;
		Page<Postagem> turmasObtidas;
		
		page = (Pageable) PageRequest.of(0, 1);
		turmasObtidas = repository.findAllByTurma(turma, page);
		assertFalse(turmasObtidas.isEmpty());
		assertTrue(turmasObtidas.isFirst());
		assertFalse(turmasObtidas.isLast());
		
		page = (Pageable) PageRequest.of(1, 1);
		turmasObtidas = repository.findAllByTurma(turma, page);
		assertFalse(turmasObtidas.isEmpty());
		assertFalse(turmasObtidas.isFirst());
		assertFalse(turmasObtidas.isLast());
		
		page = (Pageable) PageRequest.of(2, 1);
		turmasObtidas = repository.findAllByTurma(turma, page);
		assertFalse(turmasObtidas.isEmpty());
		assertFalse(turmasObtidas.isFirst());
		assertTrue(turmasObtidas.isLast());
		
		page = (Pageable) PageRequest.of(3, 1);
		turmasObtidas = repository.findAllByTurma(turma, page);
		assertTrue(turmasObtidas.isEmpty());
		
		page = (Pageable) PageRequest.of(0, 3);
		turmasObtidas = repository.findAllByTurma(turma, page);
		assertFalse(turmasObtidas.isEmpty());
		assertTrue(turmasObtidas.isFirst());
		assertTrue(turmasObtidas.isLast());
		
		page = (Pageable) PageRequest.of(1, 3);
		turmasObtidas = repository.findAllByTurma(turma, page);
		assertTrue(turmasObtidas.isEmpty());
	}
	
	@Test
	public void deveRetornarPageListaPostagemVazia()
	{
		Turma turma = new Turma();
		turma.setNome("Turma Teste");
		turma.setCodigo("39BAC6EF");
		
		entityManager.persist(turma);
		
		Pageable page;
		Page<Postagem> turmasObtidas;
		
		page = (Pageable) PageRequest.of(0, 1);
		turmasObtidas = repository.findAllByTurma(turma, page);
		assertTrue(turmasObtidas.isEmpty());
	}
}
