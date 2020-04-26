package br.com.projeto.educamais.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
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

import br.com.projeto.educamais.domain.Alternativa;
import br.com.projeto.educamais.domain.Atividade;
import br.com.projeto.educamais.domain.Pergunta;
import br.com.projeto.educamais.domain.Resposta;
import br.com.projeto.educamais.domain.Turma;
import br.com.projeto.educamais.domain.Usuario;
import br.com.projeto.educamais.exception.EntidadeInexistenteException;
import br.com.projeto.educamais.exception.UsuarioNaoTemPermissaoParaEssaAtividadeException;
import br.com.projeto.educamais.repository.AtividadeRepository;
import br.com.projeto.educamais.service.interfaces.AtividadeService;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class AtividadeServiceTest {

	@MockBean
	private AtividadeRepository repository;
	
	@Autowired
	private AtividadeService service;
	
	private final Long ATIVIDADE_ID = 1L;
	private final String ATIVIDADE_CODIGO = "39BAC6EF";
	
	private Turma turma;
	private Usuario professor;
	private Usuario aluno;
	private Atividade atividade;
	
	@BeforeEach
	public void setUp()
	{
		professor = new Usuario();
		professor.setId(1L);
		professor.setNome("professor");
		
		aluno = new Usuario();
		aluno.setId(2L);
		aluno.setNome("aluno");
		
		turma = new Turma();
		turma.setId(1L);
		turma.setProfessor(professor);
		turma.setAlunos(Arrays.asList(aluno));
		
		Alternativa alternativa_1 = new Alternativa();
		alternativa_1.setId(1L);
		alternativa_1.setTitulo("Alternativa 1");
		alternativa_1.setCorreta(true);
		
		Alternativa alternativa_2 = new Alternativa();
		alternativa_2.setId(2L);
		alternativa_2.setTitulo("Alternativa 2");
		alternativa_2.setCorreta(false);
		
		Pergunta pergunta = new Pergunta();
		pergunta.setId(1L);
		pergunta.setNota(10.0);
		pergunta.setTitulo("Titulo Pergunta");
		pergunta.setAlternativas(Arrays.asList(alternativa_1, alternativa_2));
		
		atividade = new Atividade();
		atividade.setId(ATIVIDADE_ID);
		atividade.setCodigo(ATIVIDADE_CODIGO);
		atividade.setTurma(turma);
		atividade.setAluno(aluno);
		atividade.setPerguntas(Arrays.asList(pergunta));
		atividade.setTentativas(3);
		atividade.setDataEntrega(LocalDate.now().plusDays(1L));
	}
	
	@Test
	public void professorBuscarAtividadePorTurmaDeveRetornarTodasAtividadeTodosAlunoDaquelaTurma()
	{
		Atividade atividade_1 = new Atividade();
		Atividade atividade_2 = new Atividade();
		Atividade atividade_3 = new Atividade();
		
		List<Atividade> atividades = Arrays.asList(atividade_1, atividade_2, atividade_3);
		
		when(repository.findAllByTurma(turma)).thenReturn(atividades);
		
		List<Atividade> atividadesObtidas = service.buscarPorTurma(turma, professor);
		
		assertTrue(atividades.equals(atividadesObtidas));	
	}
	
	@Test
	public void alunoBuscarAtividadePorTurmaDeveRetornarTodasAtividadeDaqueleAlunoDaquelaTurma()
	{
		Atividade atividade_1 = new Atividade();
		Atividade atividade_2 = new Atividade();
		Atividade atividade_3 = new Atividade();
		
		List<Atividade> atividades = Arrays.asList(atividade_1, atividade_2, atividade_3);
		
		when(repository.findAllByAluno(aluno)).thenReturn(atividades);
		
		List<Atividade> atividadesObtidas = service.buscarPorTurma(turma, aluno);
		
		assertTrue(atividades.equals(atividadesObtidas));
	}
	
	@Test
	public void usuarioBuscarAtividadePorTurmaQueNaoParticipaDeveLevantarException()
	{
		Usuario usuario = new Usuario();
		usuario.setId(3L);
		usuario.setNome("usuario");
		
		assertThrows(UsuarioNaoTemPermissaoParaEssaAtividadeException.class, () -> {
			service.buscarPorTurma(turma, usuario);
		});
	}
	
	@Test
	public void buscarPorIdDeveRetornarAtividade()
	{
		when(repository.findById(ATIVIDADE_ID)).thenReturn(Optional.of(atividade));
		Atividade atividadeObtida = service.buscarPorId(ATIVIDADE_ID);
		assertTrue(atividade.equals(atividadeObtida));
	}
	
	@Test
	public void buscarPorIdInvalidoDeveLevantarException()
	{
		when(repository.findById(ATIVIDADE_ID)).thenReturn(Optional.empty());
		
		assertThrows(EntidadeInexistenteException.class, () -> {
			service.buscarPorId(ATIVIDADE_ID);
		});
	}
	
	@Test
	public void professorTurmaSalvarAtividadeNaoDeveLevantarException()
	{
		Pergunta pergunta = new Pergunta();
		pergunta.setId(1L);
		pergunta.setNota(10.0);
		pergunta.setTitulo("Titulo Pergunta");
		
		Atividade atividade2 = new Atividade();
		atividade2.setTitulo("Atividade 2");
		atividade2.setDataEntrega(LocalDate.now());
		atividade2.setTentativas(2);
		atividade2.setPerguntas(Arrays.asList(pergunta));
		
		assertDoesNotThrow(() -> {
			service.salvar(turma, professor, atividade2, Arrays.asList(2L));
		});
	}
	@Test
	public void alunoTurmaSalvarAtividadeDeveLevantarException()
	{
		Pergunta pergunta = new Pergunta();
		pergunta.setId(1L);
		pergunta.setNota(10.0);
		pergunta.setTitulo("Titulo Pergunta");
		
		Atividade atividade2 = new Atividade();
		atividade2.setTitulo("Atividade 2");
		atividade2.setDataEntrega(LocalDate.now());
		atividade2.setTentativas(2);
		atividade2.setPerguntas(Arrays.asList(pergunta));
		
		assertThrows(UsuarioNaoTemPermissaoParaEssaAtividadeException.class, () -> {
			service.salvar(turma, aluno, atividade2, Arrays.asList(2L));
		});
	}
	
	@Test
	public void usuarioQueNaoParticipaTurmaSalvarAtividadeDeveLevantarException()
	{
		Usuario usuario = new Usuario();
		usuario.setId(3L);
		usuario.setNome("usuario");
		
		Pergunta pergunta = new Pergunta();
		pergunta.setId(1L);
		pergunta.setNota(10.0);
		pergunta.setTitulo("Titulo Pergunta");
		
		Atividade atividade2 = new Atividade();
		atividade2.setTitulo("Atividade 2");
		atividade2.setDataEntrega(LocalDate.now());
		atividade2.setTentativas(2);
		atividade2.setPerguntas(Arrays.asList(pergunta));
		
		assertThrows(UsuarioNaoTemPermissaoParaEssaAtividadeException.class, () -> {
			service.salvar(turma, usuario, atividade2, Arrays.asList(2L));
		});
	}
	
	@Test
	public void alunoSubmeterRespostasNaoDeveLevantarException()
	{
		Resposta resposta = new Resposta();
		resposta.setId(1L);
		resposta.setAlternativaId(1L);
		resposta.setPerguntaId(1L);
		
		when(repository.findById(ATIVIDADE_ID)).thenReturn(Optional.of(atividade));
		
		assertDoesNotThrow(() -> {
			service.submeterRespostas(Arrays.asList(resposta), turma, ATIVIDADE_ID, aluno);
		});
	}
	
	@Test
	public void professorSubmeterRespostasDeveLevantarException()
	{
		Resposta resposta = new Resposta();
		resposta.setId(1L);
		resposta.setAlternativaId(1L);
		resposta.setPerguntaId(1L);
		
		when(repository.findById(ATIVIDADE_ID)).thenReturn(Optional.of(atividade));
		
		assertThrows(UsuarioNaoTemPermissaoParaEssaAtividadeException.class, () -> {
			service.submeterRespostas(Arrays.asList(resposta), turma, ATIVIDADE_ID, professor);
		});
	}
	
	@Test
	public void usuarioNaoParticipaTurmaSubmeterRespostasDeveLevantarException()
	{
		Usuario usuario = new Usuario();
		usuario.setId(3L);
		usuario.setNome("usuario");
		
		Resposta resposta = new Resposta();
		resposta.setId(1L);
		resposta.setAlternativaId(1L);
		resposta.setPerguntaId(1L);
		
		when(repository.findById(ATIVIDADE_ID)).thenReturn(Optional.of(atividade));
		
		assertThrows(UsuarioNaoTemPermissaoParaEssaAtividadeException.class, () -> {
			service.submeterRespostas(Arrays.asList(resposta), turma, ATIVIDADE_ID, usuario);
		});
	}
	
	@Test
	public void submeterAtividadeNaoExistenteDeveLevantarException()
	{
		Resposta resposta = new Resposta();
		resposta.setId(1L);
		resposta.setAlternativaId(1L);
		resposta.setPerguntaId(1L);
		
		when(repository.findById(ATIVIDADE_ID)).thenReturn(Optional.empty());
		
		assertThrows(EntidadeInexistenteException.class, () -> {
			service.submeterRespostas(Arrays.asList(resposta), turma, ATIVIDADE_ID, aluno);
		});
	}
	
	@Test
	public void submeterAtividadeQueNaoPertenceTurmaDeveLevantarException()
	{
		Turma turma2 = new Turma();
		turma2.setId(2L);
		turma2.setNome("TURMA 2");
		
		atividade.setTurma(turma2);
		
		Resposta resposta = new Resposta();
		resposta.setId(1L);
		resposta.setAlternativaId(1L);
		resposta.setPerguntaId(1L);
		
		when(repository.findById(ATIVIDADE_ID)).thenReturn(Optional.empty());
		
		assertThrows(EntidadeInexistenteException.class, () -> {
			service.submeterRespostas(Arrays.asList(resposta), turma, ATIVIDADE_ID, aluno);
		});
	}
	
	@Test
	public void submeterAtividadeQueNaoPertenceAlunoDeveLevantarException()
	{	
		Resposta resposta = new Resposta();
		resposta.setId(1L);
		resposta.setAlternativaId(1L);
		resposta.setPerguntaId(1L);
		
		Usuario usuario = new Usuario();
		usuario.setId(3L);
		usuario.setNome("usuario");
		
		atividade.setAluno(usuario);
		
		when(repository.findById(ATIVIDADE_ID)).thenReturn(Optional.of(atividade));
		
		assertThrows(UsuarioNaoTemPermissaoParaEssaAtividadeException.class, () -> {
			service.submeterRespostas(Arrays.asList(resposta), turma, ATIVIDADE_ID, aluno);
		});
	}
	
	@Test
	public void submeterAtividadeDesabilitadaDeveLevantarException()
	{
		Resposta resposta = new Resposta();
		resposta.setId(1L);
		resposta.setAlternativaId(1L);
		resposta.setPerguntaId(1L);
		
		atividade.setTentativas(0);
		
		when(repository.findById(ATIVIDADE_ID)).thenReturn(Optional.of(atividade));
		
		assertThrows(UsuarioNaoTemPermissaoParaEssaAtividadeException.class, () -> {
			service.submeterRespostas(Arrays.asList(resposta), turma, ATIVIDADE_ID, aluno);
		});
	}
}
