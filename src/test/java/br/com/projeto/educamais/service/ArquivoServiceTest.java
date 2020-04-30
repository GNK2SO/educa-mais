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

import br.com.projeto.educamais.domain.Arquivo;
import br.com.projeto.educamais.domain.Postagem;
import br.com.projeto.educamais.domain.Turma;
import br.com.projeto.educamais.domain.Usuario;
import br.com.projeto.educamais.exception.EntidadeInexistenteException;
import br.com.projeto.educamais.exception.UsuarioNaoTemPermissaoParaEssaAtividadeException;
import br.com.projeto.educamais.repository.ArquivoRepository;
import br.com.projeto.educamais.service.interfaces.ArquivoService;
import br.com.projeto.educamais.service.interfaces.PostagemService;
import br.com.projeto.educamais.service.interfaces.TurmaService;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ArquivoServiceTest {
	
	@MockBean
	private PostagemService postagemService;
	
	@MockBean
	private TurmaService turmaService;

	@MockBean
	private ArquivoRepository arquivoRepository;
	
	@Autowired
	private ArquivoService service;
	
	private final Long TURMA_ID = 1L;
	private final Long POSTAGEM_ID = 1L;
	
	private Turma turma;
	private Usuario professor;
	private Usuario aluno;
	private Postagem postagem;
	private List<Arquivo> arquivos;
	
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
		

		turma = new Turma();
		turma.setId(TURMA_ID);
		turma.setNome("Turma Teste");
		turma.setProfessor(professor);
		turma.setAlunos(Arrays.asList(aluno));
		
		postagem = new Postagem();
		postagem.setId(POSTAGEM_ID);
		postagem.setTitulo("Titulo");
		postagem.setDescricao("Descrição");
		postagem.setArquivos(new ArrayList<Arquivo>());
		
		Arquivo arquivo_1 = new Arquivo();
		arquivo_1.setTitulo("arquivo 1");
		
		Arquivo arquivo_2 = new Arquivo();
		arquivo_2.setTitulo("arquivo 2");
		
		Arquivo arquivo_3 = new Arquivo();
		arquivo_3.setTitulo("arquivo 3");
		
		arquivos = Arrays.asList(arquivo_1, arquivo_2, arquivo_3);
	}
	
	@Test
	public void salvarDeveNArquivosDeveChamarMetodoSalvaNvezes()
	{	
		when(turmaService.buscarTurmaPorId(TURMA_ID)).thenReturn(turma);
		when(postagemService.buscarPorId(POSTAGEM_ID)).thenReturn(postagem);
		
		assertDoesNotThrow(() -> {
			service.salvar(TURMA_ID, POSTAGEM_ID, arquivos, professor);
		});
		
		verify(arquivoRepository, times(arquivos.size())).saveAndFlush(any(Arquivo.class));				
	}
	
	@Test 
	public void alunoTentarSalvarArquivosDeveLevantarException()
	{
		when(turmaService.buscarTurmaPorId(TURMA_ID)).thenReturn(turma);
		
		assertThrows(UsuarioNaoTemPermissaoParaEssaAtividadeException.class, () -> {
			service.salvar(TURMA_ID, POSTAGEM_ID, arquivos, aluno);
		});
	}
	
	@Test 
	public void usuarioNaoParticipanteTurmaSalvarArquivoDeveLevantarException() 
	{
		Usuario usuario = Usuario.builder()
							.nome("Usuario")
							.email("usuario@email.com")
							.build();
		
		when(turmaService.buscarTurmaPorId(TURMA_ID)).thenReturn(turma);
		
		assertThrows(UsuarioNaoTemPermissaoParaEssaAtividadeException.class, () -> {
			service.salvar(TURMA_ID, POSTAGEM_ID, arquivos, usuario);
		});
	}
	
	@Test 
	public void turmaIdInvalidoAoSalvarDeveLevantarException() 
	{	
		when(turmaService.buscarTurmaPorId(TURMA_ID)).thenThrow(EntidadeInexistenteException.class);
		
		assertThrows(EntidadeInexistenteException.class, () -> {
			service.salvar(TURMA_ID, POSTAGEM_ID, arquivos, professor);
		});
	}
	
	@Test 
	public void postagemIdInvalidoAoSalvarDeveLevantarException() 
	{	
		when(turmaService.buscarTurmaPorId(TURMA_ID)).thenReturn(turma);
		when(postagemService.buscarPorId(POSTAGEM_ID)).thenThrow(EntidadeInexistenteException.class);
		
		assertThrows(EntidadeInexistenteException.class, () -> {
			service.salvar(TURMA_ID, POSTAGEM_ID, arquivos, professor);
		});
	}
	
	@Test 
	public void buscarPorIdValidoDeveRetornarArquivo() 
	{
		final Long ARQUIVO_ID = 1L;
		
		Arquivo arquivo = new Arquivo();
		arquivo.setId(ARQUIVO_ID);
		arquivo.setTitulo("arquivo 1");
		
		when(arquivoRepository.findById(ARQUIVO_ID)).thenReturn(Optional.of(arquivo));
		
		assertDoesNotThrow(() -> {
			service.buscarPorId(ARQUIVO_ID);
		});
	}
	
	@Test
	public void buscarPorIdInvalidoDeveLevantarException()
	{
		final Long ARQUIVO_ID = 1L;
		
		Arquivo arquivo = new Arquivo();
		arquivo.setId(ARQUIVO_ID);
		arquivo.setTitulo("arquivo 1");
		
		when(arquivoRepository.findById(ARQUIVO_ID)).thenReturn(Optional.empty());
		
		assertThrows(EntidadeInexistenteException.class, () -> {
			service.buscarPorId(ARQUIVO_ID);
		});
	}
	
	@Test
	public void professorTentarDeletarArquivosNaoDeveLevantarException()
	{
		Arquivo arquivo = new Arquivo();
		arquivo.setId(1L);
		arquivo.setTitulo("arquivo 1");
		
		postagem.add(arquivo);
		
		when(turmaService.buscarTurmaPorId(TURMA_ID)).thenReturn(turma);
		when(postagemService.buscarPorId(POSTAGEM_ID)).thenReturn(postagem);
		
		assertDoesNotThrow(() -> {
			service.deletar(TURMA_ID, POSTAGEM_ID, arquivo, professor);
		});
	}
	
	@Test
	public void alunoTentarDeletarArquivosDeveLevantarException()
	{
		Arquivo arquivo = new Arquivo();
		arquivo.setId(1L);
		arquivo.setTitulo("arquivo 1");
		
		when(turmaService.buscarTurmaPorId(TURMA_ID)).thenReturn(turma);
		
		assertThrows(UsuarioNaoTemPermissaoParaEssaAtividadeException.class, () -> {
			service.deletar(TURMA_ID, POSTAGEM_ID, arquivo, aluno);
		});
	}
	
	@Test
	public void usuarioNaoParticipanteTurmaDeletarArquivoDeveLevantarException() 
	{
		Usuario usuario = Usuario.builder()
				.nome("Usuario")
				.email("usuario@email.com")
				.build();
		
		Arquivo arquivo = new Arquivo();
		arquivo.setId(1L);
		arquivo.setTitulo("arquivo 1");
		
		when(turmaService.buscarTurmaPorId(TURMA_ID)).thenReturn(turma);
		
		assertThrows(UsuarioNaoTemPermissaoParaEssaAtividadeException.class, () -> {
			service.deletar(TURMA_ID, POSTAGEM_ID, arquivo, usuario);
		});
	}
	
	@Test 
	public void turmaIdInvalidoAoDeletarDeveLevantarException() 
	{	
		Arquivo arquivo = new Arquivo();
		arquivo.setId(1L);
		arquivo.setTitulo("arquivo 1");
		
		when(turmaService.buscarTurmaPorId(TURMA_ID)).thenThrow(EntidadeInexistenteException.class);
		
		assertThrows(EntidadeInexistenteException.class, () -> {
			service.deletar(TURMA_ID, POSTAGEM_ID, arquivo, professor);
		});
	}
	
	@Test 
	public void postagemIdInvalidAoDeletarDeveLevantarException() 
	{	
		Arquivo arquivo = new Arquivo();
		arquivo.setId(1L);
		arquivo.setTitulo("arquivo 1");
		
		when(turmaService.buscarTurmaPorId(TURMA_ID)).thenReturn(turma);
		when(postagemService.buscarPorId(POSTAGEM_ID)).thenThrow(EntidadeInexistenteException.class);
		
		assertThrows(EntidadeInexistenteException.class, () -> {
			service.deletar(TURMA_ID, POSTAGEM_ID, arquivo, professor);
		});
	}
	
	@Test
	public void deletarArquivoQueNaoPertencePostagemDeveLevantarException()
	{
		Arquivo arquivo = new Arquivo();
		arquivo.setId(1L);
		arquivo.setTitulo("arquivo 1");
		
		when(turmaService.buscarTurmaPorId(TURMA_ID)).thenReturn(turma);
		when(postagemService.buscarPorId(POSTAGEM_ID)).thenReturn(postagem);
		
		assertThrows(EntidadeInexistenteException.class, () -> {
			service.deletar(TURMA_ID, POSTAGEM_ID, arquivo, professor);
		});
	}
}
