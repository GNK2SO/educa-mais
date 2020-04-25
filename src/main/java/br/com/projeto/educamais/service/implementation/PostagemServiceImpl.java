package br.com.projeto.educamais.service.implementation;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import br.com.projeto.educamais.domain.Postagem;
import br.com.projeto.educamais.domain.Turma;
import br.com.projeto.educamais.domain.Usuario;
import br.com.projeto.educamais.exception.EntidadeInexistenteException;
import br.com.projeto.educamais.exception.UsuarioNaoTemPermissaoParaEssaAtividadeException;
import br.com.projeto.educamais.repository.PostagemRepository;
import br.com.projeto.educamais.service.GenericService;
import br.com.projeto.educamais.service.interfaces.PostagemService;
import br.com.projeto.educamais.util.messages.PostagemErrors;
import br.com.projeto.educamais.util.messages.TurmaErrors;

@Service
public class PostagemServiceImpl extends GenericService implements PostagemService{

	
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
	public List<Postagem> buscarPorTurma(Turma turma, Usuario usuario) {
		
		if(turma.professorIsNotEqualTo(usuario) && turma.notContains(usuario)) {
			throw new UsuarioNaoTemPermissaoParaEssaAtividadeException(TurmaErrors.FORBIDDEN_NOT_PARTICIPATE);
		}
		
		return repository.findAllByTurma(turma);
	}
	
	@Transactional
	public List<Postagem> buscarPostagensPageablePorTurma(Turma turma, int pageNumber, Usuario usuario) {
		
		if(turma.professorIsNotEqualTo(usuario) && turma.notContains(usuario)) {
			throw new UsuarioNaoTemPermissaoParaEssaAtividadeException(TurmaErrors.FORBIDDEN_NOT_PARTICIPATE);
		}
		
		Pageable page = (Pageable) PageRequest.of(pageNumber, 20);
		Page<Postagem> postagens = repository.findAllByTurma(turma, page);
		
		return postagens.getContent();
	}

	@Transactional
	public void atualizarPostagem(Turma turma, Usuario usuario, Postagem postagem) {
		
		if(turma.professorIsNotEqualTo(usuario)) {
			throw new UsuarioNaoTemPermissaoParaEssaAtividadeException(PostagemErrors.FORBIDDEN_ATUALIZAR_POSTAGEM);
		}
		
		Postagem postagemOld = buscarPorId(postagem.getId());
		
		if(postagemOld.notBelongsTo(turma))
		{
			throw new UsuarioNaoTemPermissaoParaEssaAtividadeException(PostagemErrors.FORBIDDEN_ATUALIZAR_POSTAGEM);
		}
		
		postagemOld.setTitulo(postagem.getTitulo());
		postagemOld.setDescricao(postagem.getDescricao());
		
		preencherCamposAuditoria(postagem, usuario);	
	}
	
	

	@Transactional
	public void deletarPostagem(Turma turma, Usuario usuario, Long postagemId) {

		if(turma.professorIsNotEqualTo(usuario)) {
			throw new UsuarioNaoTemPermissaoParaEssaAtividadeException(PostagemErrors.FORBIDDEN_REMOVER_POSTAGEM);
		}
	
		Postagem postagem = buscarPorId(postagemId);
		
		if(postagem.notBelongsTo(turma))
		{
			throw new UsuarioNaoTemPermissaoParaEssaAtividadeException(PostagemErrors.FORBIDDEN_ATUALIZAR_POSTAGEM);
		}
		
		repository.delete(postagem);
	}
}
