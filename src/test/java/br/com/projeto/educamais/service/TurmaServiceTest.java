package br.com.projeto.educamais.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.projeto.educamais.domain.Postagem;
import br.com.projeto.educamais.domain.Turma;
import br.com.projeto.educamais.domain.Usuario;
import br.com.projeto.educamais.exception.EntidadeExistenteException;
import br.com.projeto.educamais.exception.EntidadeInexistenteException;
import br.com.projeto.educamais.exception.ProfessorNaoPodeSerAlunoException;
import br.com.projeto.educamais.exception.UsuarioJaEstaNaTurmaException;
import br.com.projeto.educamais.exception.UsuarioNaoTemPermissaoParaEssaAtividadeException;
import br.com.projeto.educamais.repository.TurmaRepository;
import br.com.projeto.educamais.service.interfaces.PostagemService;
import br.com.projeto.educamais.service.interfaces.TurmaService;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TurmaServiceTest {

	@MockBean
	private TurmaRepository repository;
	
	@MockBean
	private PostagemService postagemService;
	
	@Autowired
	private TurmaService service;
	
	private final Long TURMA_ID = 1L;
	private final String TURMA_NOME = "Turma Teste";
	private final String TURMA_CODIGO = "39BAC6EF";
	private Turma turma;
	private Usuario professor;
	private Usuario aluno;
	
	
	@BeforeEach
	public void setUp()
	{
		professor = Usuario.builder()
				.nome("professor")
				.email("professor@email.com")
				.build();
		
		aluno = Usuario.builder()
				.nome("aluno")
				.email("aluno@email.com")
				.build();	
		
		
		List<Usuario> usuarios = new ArrayList<Usuario>();
		usuarios.add(aluno);
		
		turma = new Turma();
		turma.setId(TURMA_ID);
		turma.setNome(TURMA_NOME);
		turma.setCodigo(TURMA_CODIGO);
		turma.setProfessor(professor);
		turma.setAlunos(usuarios);
	}
	
	@Test
	public void salvarTurmaComNomeExistenteDeveLevantarException()
	{
		when(repository.existsByNome(TURMA_NOME)).thenReturn(true);
		
		assertThrows(EntidadeExistenteException.class, () -> {
			service.salva(turma);
		});
	}
	
	@Test
	public void deveRetornarListaTurmas()
	{
		Turma turma_1 = new Turma();
		Turma turma_2 = new Turma();
		Turma turma_3 = new Turma();
		
		List<Turma> turmas = Arrays.asList(turma_1,turma_2,turma_3);
		
		when(repository.findByProfessorOrAlunosContaining(any(Usuario.class), any(Usuario.class)))
		.thenReturn(turmas);
		
		List<Turma> turmasObtidas = service.buscarTurmas(new Usuario());
		
		assertEquals(turmas, turmasObtidas);
	}
	
	@Test
	public void deveRetornarTurmaPeloId()
	{
		when(repository.findById(TURMA_ID)).thenReturn(Optional.of(turma));
		Turma turmaObtida = service.buscarTurmaPorId(TURMA_ID);
		assertTrue(turma.equals(turmaObtida));
	}
	
	@Test
	public void idInvalidoDeveRetornarException()
	{
		when(repository.findById(TURMA_ID)).thenReturn(Optional.empty());
		assertThrows(EntidadeInexistenteException.class, () -> {
			service.buscarTurmaPorId(TURMA_ID);
		});
	}
	
	@Test
	public void deveRetornarTurmaPeloCodigo()
	{
		when(repository.findByCodigo(TURMA_CODIGO)).thenReturn(Optional.of(turma));
		Turma turmaObtida = service.buscarTurmaPorCodigo(TURMA_CODIGO);
		assertTrue(turma.equals(turmaObtida));
	}
	
	@Test
	public void codigoInvalidoDeveRetornarException()
	{
		when(repository.findByCodigo(TURMA_CODIGO)).thenReturn(Optional.empty());
		assertThrows(EntidadeInexistenteException.class, () -> {
			service.buscarTurmaPorCodigo(TURMA_CODIGO);
		});
	}
	
	@Test
	public void atualizarTurmaComSucessoDeveChamarMetodoSaveRepository()
	{
		assertDoesNotThrow(() -> {
			service.atualizarDados(turma);	
		});
	}
	
	@Test
	public void participarTurmaDeveChamarMetodoSaveAndFlush()
	{
		Usuario usuarioNovo = Usuario.builder()
								.nome("NOVO_USUARIO")
								.email("novo_usuario@email.com")
								.build();
		
		service.participar(turma, usuarioNovo);
		verify(repository).saveAndFlush(any(Turma.class));
	}
	
	@Test
	public void casoProfessorTentarParticiparPropriaSalaDeveRetornarException()
	{
		assertThrows(ProfessorNaoPodeSerAlunoException.class, () -> {
			service.participar(turma, professor);
		});
	}
	
	@Test
	public void casoAlunoTentarParticiparQueJaParticipaDeveRetornarException()
	{
		assertThrows(UsuarioJaEstaNaTurmaException.class, () -> {
			service.participar(turma, aluno);
		});
	}
	
	@Test
	public void alunoSairTurmaNaoDeveLevantarException()
	{
		assertDoesNotThrow(() -> {
			service.sairTurma(turma, aluno);
		});
	}
	
	@Test
	public void alunoNaoPertenceTurmaSairDeveLevantarException()
	{
		Usuario usuarioNovo = Usuario.builder()
				.nome("NOVO_USUARIO")
				.email("novo_usuario@email.com")
				.build();
		
		assertThrows(UsuarioNaoTemPermissaoParaEssaAtividadeException.class, () -> {
			service.sairTurma(turma, usuarioNovo);
		});
	}
	
	@Test
	public void professorDeletarTurmaNaoDeveLevantarException()
	{
		when(repository.findById(TURMA_ID)).thenReturn(Optional.of(turma));
		when(postagemService.buscarPorTurma(turma, professor)).thenReturn(new ArrayList<Postagem>());
		
		assertDoesNotThrow(() -> {
			service.deletar(TURMA_ID, professor);
		});
	}
	
	@Test
	public void usuarioDiferenteProfessorTentarDeletarTurmaDeveLevantarException()
	{
		when(repository.findById(TURMA_ID)).thenReturn(Optional.of(turma));
		Usuario usuarioNovo = Usuario.builder()
				.nome("NOVO_USUARIO")
				.email("novo_usuario@email.com")
				.build();
		
		assertThrows(UsuarioNaoTemPermissaoParaEssaAtividadeException.class, () -> {
			service.deletar(TURMA_ID, usuarioNovo);
		});
	}
	
	@Test
	public void idTurmaInvalidoDeveLevantarException()
	{
		when(repository.findById(TURMA_ID)).thenReturn(Optional.empty());
		
		assertThrows(EntidadeInexistenteException.class, () -> {
			service.deletar(TURMA_ID, professor);
		});
	}
}
