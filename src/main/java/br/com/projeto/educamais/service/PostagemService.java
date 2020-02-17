package br.com.projeto.educamais.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.projeto.educamais.controller.postagem.form.AtualizarPostagemForm;
import br.com.projeto.educamais.domain.Postagem;
import br.com.projeto.educamais.domain.Turma;
import br.com.projeto.educamais.domain.Usuario;
import br.com.projeto.educamais.exception.EntidadeInexistenteException;
import br.com.projeto.educamais.exception.UsuarioNaoTemPermissaoParaEssaAtividadeException;
import br.com.projeto.educamais.repository.PostagemRepository;
import br.com.projeto.educamais.util.messages.PostagemErrors;
import br.com.projeto.educamais.util.messages.TurmaErrors;

@Service
public class PostagemService extends GenericService {

	@Autowired
	private TurmaService turmaService;
	
	@Autowired
	private PostagemRepository repository;
	

	@Transactional
	public Turma buscarPostagensTurma(Long turmaId, Usuario usuarioLogado) {
		Turma turma = turmaService.buscarTurmaPorId(turmaId);
		
		if(turma.professorIsNotEqualTo(usuarioLogado) && turma.notContains(usuarioLogado)) {
			throw new UsuarioNaoTemPermissaoParaEssaAtividadeException(TurmaErrors.FORBIDDEN_NOT_PARTICIPATE);
		}
		
		return turma;
	}
	
	@Transactional
	public Postagem salvar(Long turmaId, Usuario usuario, Postagem postagem) {
		
		Turma turma = turmaService.buscarTurmaPorId(turmaId);
		
		if(turma.professorIsNotEqualTo(usuario)) {
			throw new UsuarioNaoTemPermissaoParaEssaAtividadeException(PostagemErrors.FORBIDDEN_SALVAR_POSTAGEM);
		}
		
		preencherCamposAuditoria(postagem, turma.getProfessor());
		postagem = repository.saveAndFlush(postagem);
		
		turma.add(postagem);
		turmaService.atualizarDados(turma);
		
		return postagem;
	}
	
	@Transactional
	public Postagem buscarPorId(Long id) {
		Optional<Postagem> postagem = repository.findById(id);
		if(postagem.isPresent()) {
			return postagem.get();
		}
		throw new EntidadeInexistenteException(PostagemErrors.NOT_FOUND);
	}

	@Transactional
	public void atualizarPostagem(Long turmaId, Usuario usuario, Long postagemId, AtualizarPostagemForm form) {
		Turma turma = turmaService.buscarTurmaPorId(turmaId);
		
		if(turma.professorIsNotEqualTo(usuario)) {
			throw new UsuarioNaoTemPermissaoParaEssaAtividadeException(PostagemErrors.FORBIDDEN_ATUALIZAR_POSTAGEM);
		}
		
		Postagem postagem = buscarPorId(postagemId);
		postagem.setTitulo(form.getTitulo());
		postagem.setDescricao(form.getDescricao());
		
		preencherCamposAuditoria(postagem, usuario);
		
	}
	
	@Transactional
	public void atualizarPostagem(Postagem postagem, Usuario usuario) {
		preencherCamposAuditoria(postagem, usuario);
	}

	@Transactional
	public void deletarPostagem(Long turmaId, Usuario usuario, Long postagemId) {
		Turma turma = turmaService.buscarTurmaPorId(turmaId);
		
		if(turma.professorIsNotEqualTo(usuario)) {
			throw new UsuarioNaoTemPermissaoParaEssaAtividadeException(PostagemErrors.FORBIDDEN_REMOVER_POSTAGEM);
		}
		
		Postagem postagem = buscarPorId(postagemId);
		
		repository.deleteById(postagem.getId());
	}

	
}
