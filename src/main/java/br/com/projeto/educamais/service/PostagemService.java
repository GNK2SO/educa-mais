package br.com.projeto.educamais.service;

import java.util.List;
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
	private PostagemRepository repository;
	
	@Transactional
	public Postagem salvar(Turma turma, Usuario usuario, Postagem postagem) {
		
		if(turma.professorIsNotEqualTo(usuario)) {
			throw new UsuarioNaoTemPermissaoParaEssaAtividadeException(PostagemErrors.FORBIDDEN_SALVAR_POSTAGEM);
		}
		
		preencherCamposAuditoria(postagem, turma.getProfessor());
		postagem.setTurma(turma);
		postagem = repository.saveAndFlush(postagem);
		
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
	public List<Postagem> buscarPorTurma(Turma turma, Usuario usuarioLogado) {
		
		if(turma.professorIsNotEqualTo(usuarioLogado) && turma.notContains(usuarioLogado)) {
			throw new UsuarioNaoTemPermissaoParaEssaAtividadeException(TurmaErrors.FORBIDDEN_NOT_PARTICIPATE);
		}
		
		return repository.findAllByTurma(turma);
	}

	@Transactional
	public void atualizarPostagem(Turma turma, Usuario usuario, Long postagemId, AtualizarPostagemForm form) {
		
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
	public void deletarPostagem(Turma turma, Usuario usuario, Long postagemId) {

		if(turma.professorIsNotEqualTo(usuario)) {
			throw new UsuarioNaoTemPermissaoParaEssaAtividadeException(PostagemErrors.FORBIDDEN_REMOVER_POSTAGEM);
		}
		
		Postagem postagem = buscarPorId(postagemId);
		
		repository.deleteById(postagem.getId());
	}

	
}
