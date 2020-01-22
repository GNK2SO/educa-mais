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

@Service
public class PostagemService extends GenericService {

	@Autowired
	public TurmaService turmaService;
	
	@Autowired
	public PostagemRepository repository;
	
	@Transactional
	public Postagem salvar(Long turmaId, Usuario usuario, Postagem postagem) {
		
		Turma turma = turmaService.obterTurmaPorId(turmaId);
		
		if(turma.professorIsNotEqualTo(usuario)) {
			throw new UsuarioNaoTemPermissaoParaEssaAtividadeException("Apenas o professor tem permissão para cadastrar postagens.");
		}
		
		preencherCamposAuditoria(postagem, turma.getProfessor());
		postagem = repository.saveAndFlush(postagem);
		
		turma.add(postagem);
		turmaService.atualizarDados(turma);
		
		return postagem;
	}
	
	@Transactional
	public Postagem obterPorId(Long id) {
		Optional<Postagem> postagem = repository.findById(id);
		if(postagem.isPresent()) {
			return postagem.get();
		}
		throw new EntidadeInexistenteException("Falha ao obter postagem. Postagem não está cadastrada.");
	}

	@Transactional
	public void atualizarPostagem(Long turmaId, Usuario usuario, Long postagemId, AtualizarPostagemForm form) {
		Turma turma = turmaService.obterTurmaPorId(turmaId);
		
		if(turma.professorIsNotEqualTo(usuario)) {
			throw new UsuarioNaoTemPermissaoParaEssaAtividadeException("Apenas o professor tem permissão para alterar postagens.");
		}
		
		Postagem postagem = obterPorId(postagemId);
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
		Turma turma = turmaService.obterTurmaPorId(turmaId);
		
		if(turma.professorIsNotEqualTo(usuario)) {
			throw new UsuarioNaoTemPermissaoParaEssaAtividadeException("Apenas o professor tem permissão para deletar postagens.");
		}
		
		Postagem postagem = obterPorId(postagemId);
		
		repository.deleteById(postagem.getId());
	}
	
}
