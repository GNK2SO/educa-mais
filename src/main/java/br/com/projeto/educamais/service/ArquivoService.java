package br.com.projeto.educamais.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.projeto.educamais.domain.Arquivo;
import br.com.projeto.educamais.domain.Postagem;
import br.com.projeto.educamais.domain.Turma;
import br.com.projeto.educamais.domain.Usuario;
import br.com.projeto.educamais.exception.EntidadeInexistenteException;
import br.com.projeto.educamais.exception.UsuarioNaoTemPermissaoParaEssaAtividadeException;
import br.com.projeto.educamais.repository.ArquivoRepository;

@Service
public class ArquivoService {

	@Autowired
	private PostagemService postagemService;
	
	@Autowired
	private TurmaService turmaService;
	
	@Autowired
	private ArquivoRepository repository;
	
	
	@Transactional
	public void salvar(Long turmaId, Long postagemId, List<Arquivo> arquivos, Usuario usuario) {
		
		Turma turma = turmaService.obterTurmaPorId(turmaId);
		
		if(turma.professorIsNotEqualTo(usuario)) {
			throw new UsuarioNaoTemPermissaoParaEssaAtividadeException("Apenas o professor tem permissão para deletar postagens.");
		}
		
		Postagem postagem = postagemService.obterPorId(postagemId);
		
		for (Arquivo arquivo : arquivos) {
			Arquivo arquivoSalvo = repository.saveAndFlush(arquivo);
			postagem.add(arquivoSalvo);
		}
		
		postagemService.atualizarPostagem(postagem, usuario);
	}
	
	@Transactional
	public Arquivo obterPorId(Long arquivoId) {
		
		Optional<Arquivo> arquivo = repository.findById(arquivoId);
		
		if(arquivo.isPresent()) {
			return arquivo.get();
		}
		throw new EntidadeInexistenteException("Falha ao obter arquivo. Arquivo não está cadastrado.");
	}

	@Transactional
	public void deletar(Long turmaId, Long postagemId, Arquivo arquivo, Usuario usuario) {
		
		Turma turma = turmaService.obterTurmaPorId(turmaId);
		
		if(turma.professorIsNotEqualTo(usuario)) {
			throw new UsuarioNaoTemPermissaoParaEssaAtividadeException("Apenas o professor tem permissão para deletar postagens.");
		}
		
		Postagem postagem = postagemService.obterPorId(postagemId);
		
		if(postagem.notContains(arquivo)) {
			throw new EntidadeInexistenteException("Falha ao deletar arquivo. Este arquivo não está associado a postagem informada.");
		}
		postagem.remove(arquivo);
		postagemService.atualizarPostagem(postagem, usuario);
	}
}
