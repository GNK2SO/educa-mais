package br.com.projeto.educamais.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.projeto.educamais.domain.Postagem;
import br.com.projeto.educamais.domain.Turma;
import br.com.projeto.educamais.domain.Usuario;
import br.com.projeto.educamais.exception.EntidadeInexistenteException;
import br.com.projeto.educamais.exception.UsuarioNaoTemPermissaoParaEssaAtividadeException;
import br.com.projeto.educamais.repository.PostagemRepository;
import br.com.projeto.educamais.service.interfaces.PostagemService;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class PostagemServiceTest {

	@MockBean
	private PostagemRepository repository;
	
	@Autowired
	private PostagemService service;

	private final Long TURMA_ID = 1L;
	private final Long POSTAGEM_ID = 1L;
	
	private Turma turma;
	private Usuario professor;
	private Usuario aluno;
	private Postagem postagem;
	
	@BeforeEach
	public void setUp()
	{
		aluno = Usuario.builder()
				.nome("aluno")
				.email("aluno@email.com")
				.build();
		
		professor = Usuario.builder()
				.nome("professor")
				.email("professor@email.com")
				.build();
			
		turma = new Turma();
		turma.setId(TURMA_ID);
		turma.setNome("TURMA TESTE");
		turma.setCodigo("39BAC6EF");
		turma.setProfessor(professor);
		turma.setAlunos(Arrays.asList(aluno));
		
		postagem = Postagem.builder()
				.titulo("Titulo Postagem")
				.descricao("Descrição Postagem")
				.turma(turma)
				.build();
	}
	
	@Test
	public void professorSalvarPostagemNaoDeveLevantarException()
	{
		assertDoesNotThrow(() -> {
			service.salvar(turma, professor, postagem);
		});
	}
	
	@Test
	public void usuarioDiferenteProfessorSalvarPostagemDeveLevantarException()
	{
		Usuario usuario = Usuario.builder()
				.nome("usuario")
				.email("usuario@email.com")
				.build();
		
		assertThrows(UsuarioNaoTemPermissaoParaEssaAtividadeException.class, () -> {
			service.salvar(turma, usuario, postagem);
		});
	}
	
	@Test
	public void idValidoDeveRetonarPostagem()
	{
		when(repository.findById(POSTAGEM_ID)).thenReturn(Optional.of(postagem));
		Postagem postagemObtida = service.buscarPorId(POSTAGEM_ID);
		assertTrue(postagem.equals(postagemObtida));
	}
	
	@Test
	public void idInvalidoDeveLevantarException()
	{
		when(repository.findById(POSTAGEM_ID)).thenReturn(Optional.empty());
		assertThrows(EntidadeInexistenteException.class, () -> {
			service.buscarPorId(POSTAGEM_ID);
		});
	}
	
	@Test
	public void obterPostagemPorTurmaComProfessorDeveRetonarListaPostagem()
	{
		Postagem postagem_1 = new Postagem();
		Postagem postagem_2 = new Postagem();
		Postagem postagem_3 = new Postagem();
		
		List<Postagem> postagens = new ArrayList<Postagem>();
		postagens.add(postagem_1);
		postagens.add(postagem_2);
		postagens.add(postagem_3);
		
		when(repository.findAllByTurma(turma)).thenReturn(postagens);
		List<Postagem> postagensObtidas = service.buscarPorTurma(turma, professor);
		assertTrue(postagens.equals(postagensObtidas));
	}
	
	@Test
	public void obterPostagemPorTurmaComAlunoDeveRetonarListaPostagem()
	{
		Postagem postagem_1 = new Postagem();
		Postagem postagem_2 = new Postagem();
		Postagem postagem_3 = new Postagem();
		
		List<Postagem> postagens = new ArrayList<Postagem>();
		postagens.add(postagem_1);
		postagens.add(postagem_2);
		postagens.add(postagem_3);
		
		when(repository.findAllByTurma(turma)).thenReturn(postagens);
		List<Postagem> postagensObtidas = service.buscarPorTurma(turma, aluno);
		assertTrue(postagens.equals(postagensObtidas));
	}
	
	@Test
	public void obterPostagemPorTurmaSemParticiparTurmaDeveLevantarException()
	{
		Usuario usuario = Usuario.builder()
				.nome("usuario")
				.email("usuario@email.com")
				.build();
		
		assertThrows(UsuarioNaoTemPermissaoParaEssaAtividadeException.class, () -> {
			service.buscarPorTurma(turma, usuario);
		});
	}
	
	@Test
	public void obterPostagemPageablePorTurmaComProfessorDeveRetonarListaPostagem()
	{
		Postagem postagem_1 = new Postagem();
		Postagem postagem_2 = new Postagem();
		Postagem postagem_3 = new Postagem();
		
		List<Postagem> postagens = new ArrayList<Postagem>();
		postagens.add(postagem_1);
		postagens.add(postagem_2);
		postagens.add(postagem_3);
		
		Pageable page = (Pageable) PageRequest.of(1, 20);
		Page<Postagem> pagePostagens = new PageImpl<Postagem>(postagens);
		
		when(repository.findAllByTurma(turma, page)).thenReturn(pagePostagens);
		
		List<Postagem> postagensObtidas = service.buscarPostagensPageablePorTurma(turma, 1, professor);
		assertTrue(postagens.equals(postagensObtidas));
	}
	
	@Test
	public void obterPostagemPageablePorTurmaComAlunoDeveRetonarListaPostagem()
	{
		Postagem postagem_1 = new Postagem();
		Postagem postagem_2 = new Postagem();
		Postagem postagem_3 = new Postagem();
		
		List<Postagem> postagens = new ArrayList<Postagem>();
		postagens.add(postagem_1);
		postagens.add(postagem_2);
		postagens.add(postagem_3);
		
		Pageable page = (Pageable) PageRequest.of(1, 20);
		Page<Postagem> pagePostagens = new PageImpl<Postagem>(postagens);
		
		when(repository.findAllByTurma(turma, page)).thenReturn(pagePostagens);
		
		List<Postagem> postagensObtidas = service.buscarPostagensPageablePorTurma(turma, 1, aluno);
		assertTrue(postagens.equals(postagensObtidas));
	}
	
	@Test
	public void obterPostagemPageablePorTurmaSemParticiparTurmaDeveLevantarException()
	{
		Usuario usuario = Usuario.builder()
				.nome("usuario")
				.email("usuario@email.com")
				.build();
		
		assertThrows(UsuarioNaoTemPermissaoParaEssaAtividadeException.class, () -> {
			service.buscarPostagensPageablePorTurma(turma, 1, usuario);
		});
	}
	
	@Test
	public void atualizarPostagemNaoDeveLevantarException()
	{
		Postagem postagemAtualizada = new Postagem();
		postagemAtualizada.setId(POSTAGEM_ID);
		postagemAtualizada.setTitulo("NOVO_TITULO");
		postagemAtualizada.setDescricao("NOVA_DESCRICAO");
		
		when(repository.findById(POSTAGEM_ID)).thenReturn(Optional.of(postagem));
		assertDoesNotThrow(() -> {
			service.atualizarPostagem(turma, professor, postagemAtualizada);
		});
	}
	
	@Test
	public void alunoTentarAtualizarPostagemDeveLevantarException()
	{
		Postagem postagemAtualizada = new Postagem();
		postagemAtualizada.setId(POSTAGEM_ID);
		postagemAtualizada.setTitulo("NOVO_TITULO");
		postagemAtualizada.setDescricao("NOVA_DESCRICAO");
		
		assertThrows(UsuarioNaoTemPermissaoParaEssaAtividadeException.class, () -> {
			service.atualizarPostagem(turma, aluno, postagemAtualizada);
		});
	}
	
	@Test
	public void atualizarPostagemComIdInvalidoDeveLevantarException()
	{
		Postagem postagemAtualizada = new Postagem();
		postagemAtualizada.setId(POSTAGEM_ID);
		postagemAtualizada.setTitulo("NOVO_TITULO");
		postagemAtualizada.setDescricao("NOVA_DESCRICAO");
		
		when(repository.findById(POSTAGEM_ID)).thenReturn(Optional.empty());
		
		assertThrows(EntidadeInexistenteException.class, () -> {
			service.atualizarPostagem(turma, professor, postagemAtualizada);
		});
	}
	
	@Test
	public void professorAtualizarPostagemOutraTurmaDeveLevantarException()
	{
		
		Postagem postagemAtualizada = new Postagem();
		postagemAtualizada.setId(POSTAGEM_ID);
		postagemAtualizada.setTitulo("NOVO_TITULO");
		postagemAtualizada.setDescricao("NOVA_DESCRICAO");
		
		Turma turma2 = new Turma();
		turma2.setProfessor(aluno);
		
		postagem.setTurma(turma2);
		
		when(repository.findById(POSTAGEM_ID)).thenReturn(Optional.of(postagem));
		
		assertThrows(UsuarioNaoTemPermissaoParaEssaAtividadeException.class, () -> {
			service.atualizarPostagem(turma, aluno, postagemAtualizada);
		});
	}
	

	@Test
	public void deletarPostagemNaoDeveLevantarException()
	{
		when(repository.findById(POSTAGEM_ID)).thenReturn(Optional.of(postagem));
		assertDoesNotThrow(() -> {
			service.deletarPostagem(turma, professor, POSTAGEM_ID);
		});
	}
	
	@Test
	public void alunoTentarDeletarPostagemDeveLevantarException()
	{
		assertThrows(UsuarioNaoTemPermissaoParaEssaAtividadeException.class, () -> {
			service.deletarPostagem(turma, aluno, POSTAGEM_ID);
		});
	}
	
	@Test
	public void deletarPostagemComIdInvalidoDeveLevantarException()
	{
		when(repository.findById(POSTAGEM_ID)).thenReturn(Optional.empty());
		assertThrows(EntidadeInexistenteException.class, () -> {
			service.deletarPostagem(turma, professor, POSTAGEM_ID);
		});
	}
	
	@Test
	public void professorDeletarPostagemOutraTurmaDeveLevantarException()
	{
		Turma turma2 = new Turma();
		turma2.setProfessor(aluno);
		
		postagem.setTurma(turma2);
		
		when(repository.findById(POSTAGEM_ID)).thenReturn(Optional.of(postagem));
		
		assertThrows(UsuarioNaoTemPermissaoParaEssaAtividadeException.class, () -> {
			service.deletarPostagem(turma, aluno, POSTAGEM_ID);
		});
	}
}
