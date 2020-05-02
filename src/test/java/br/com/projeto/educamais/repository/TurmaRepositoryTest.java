package br.com.projeto.educamais.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

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

import br.com.projeto.educamais.domain.Turma;
import br.com.projeto.educamais.domain.Usuario;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ExtendWith(SpringExtension.class)
public class TurmaRepositoryTest {

	@Autowired
	private TurmaRepository repository;
	
	@Autowired
	private TestEntityManager entityManager;

	private final String NOME_TURMA = "Turma Teste";
	private final String CODIGO_TURMA = "39BAC6EF";
	
	@Test
	public void deveRetornarTrueQuandoVerificarExistenciaNome()
	{
		Turma turma = new Turma();
		turma.setNome(NOME_TURMA);
		
		entityManager.persist(turma);
		
		boolean existByNome = repository.existsByNome(NOME_TURMA);
		assertTrue(existByNome);
	}
	
	@Test
	public void deveRetornarFalseQuandoVerificarExistenciaNome()
	{
		boolean existByNome = repository.existsByNome(NOME_TURMA);
		assertFalse(existByNome);
	}
	
	@Test
	public void deveRetornarTrueQuandoVerificarExistenciaCodigo()
	{
		Turma turma = new Turma();
		turma.setCodigo(CODIGO_TURMA);
		
		entityManager.persist(turma);
		
		boolean existByCodigo = repository.existsByCodigo(CODIGO_TURMA);
		assertTrue(existByCodigo);
	}
	
	@Test
	public void deveRetornarFalseQuandoVerificarExistenciaCodigo()
	{
		boolean existByCodigo = repository.existsByCodigo(CODIGO_TURMA);
		assertFalse(existByCodigo);
	}
	
	@Test
	public void deveRetornarTodasTurmasQueUsuarioParticiparComoProfessorOuAluno()
	{
		Usuario usuario = new Usuario();
		usuario.setNome("usuario");
		usuario.setEmail("usuario@email.com");
		
		Turma turmaProfessor = new Turma();
		turmaProfessor.setNome("Turma Professor");
		turmaProfessor.setProfessor(usuario);
		
		Turma turmaAluno = new Turma();
		turmaAluno.setNome("Turma Aluno");
		turmaAluno.setAlunos(Arrays.asList(usuario));
		
		entityManager.persist(usuario);
		entityManager.persist(turmaProfessor);
		entityManager.persist(turmaAluno);
		
		List<Turma> turmasObtidas = repository.findByProfessorOrAlunosContaining(usuario, usuario);
		
		assertTrue(turmasObtidas.containsAll(Arrays.asList(turmaProfessor, turmaAluno)));
	}
	
	@Test
	public void deveRetornarListTurmaVazia()
	{
		Usuario usuario = new Usuario();
		usuario.setNome("usuario");
		usuario.setEmail("usuario@email.com");
		
		entityManager.persist(usuario);
		
		List<Turma> turmasObtidas = repository.findByProfessorOrAlunosContaining(usuario, usuario);
		
		assertTrue(turmasObtidas.isEmpty());
	}
	
	@Test
	public void deveRetornarOptinalPreenchidoQuandoBuscarCodigo()
	{
		Turma turma = new Turma();
		turma.setCodigo(CODIGO_TURMA);
		
		entityManager.persist(turma);
		
		Optional<Turma> turmaOptional = repository.findByCodigo(CODIGO_TURMA);
		assertTrue(turmaOptional.isPresent());
		
		Turma turmaObtida = turmaOptional.get();
		assertEquals(turma.getCodigo(), turmaObtida.getCodigo());
	}
	
	@Test
	public void deveRetornarOptinalVazioQuandoBuscarCodigo()
	{
		Optional<Turma> turma = repository.findByCodigo(CODIGO_TURMA);
		assertFalse(turma.isPresent());
	}
}
