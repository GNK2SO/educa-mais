package br.com.projeto.educamais.service.implementation;

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
import br.com.projeto.educamais.service.GenericService;
import br.com.projeto.educamais.service.interfaces.ArquivoService;
import br.com.projeto.educamais.service.interfaces.PostagemService;
import br.com.projeto.educamais.service.interfaces.TurmaService;
import br.com.projeto.educamais.util.messages.ArquivoErrors;
import br.com.projeto.educamais.util.messages.PostagemErrors;

@Service
public class ArquivoServiceImpl extends GenericService implements ArquivoService{

	@Autowired
	private PostagemService postagemService;
	
	@Autowired
	private TurmaService turmaService;
	
	@Autowired
	private ArquivoRepository repository;
	
	
	@Transactional
	public void salvar(Long turmaId, Long postagemId, List<Arquivo> arquivos, Usuario usuario) {
		
		Turma turma = turmaService.buscarTurmaPorId(turmaId);
		
		if(turma.professorIsNotEqualTo(usuario)) {
			throw new UsuarioNaoTemPermissaoParaEssaAtividadeException(PostagemErrors.FORBIDDEN_REMOVER_POSTAGEM);
		}
		
		Postagem postagem = postagemService.buscarPorId(postagemId);
		
		for (Arquivo arquivo : arquivos) {
			Arquivo arquivoSalvo = repository.saveAndFlush(arquivo);
			postagem.add(arquivoSalvo);
		}
		
		preencherCamposAuditoria(postagem, usuario);
	}
	
	@Transactional
	public Arquivo buscarPorId(Long arquivoId) {
		
		Optional<Arquivo> arquivo = repository.findById(arquivoId);
		
		if(arquivo.isPresent()) {
			return arquivo.get();
		}
		throw new EntidadeInexistenteException(ArquivoErrors.NOT_FOUND);
	}

	@Transactional
	public void deletar(Long turmaId, Long postagemId, Arquivo arquivo, Usuario usuario) {
		
		Turma turma = turmaService.buscarTurmaPorId(turmaId);
		
		if(turma.professorIsNotEqualTo(usuario)) {
			throw new UsuarioNaoTemPermissaoParaEssaAtividadeException(PostagemErrors.FORBIDDEN_REMOVER_POSTAGEM);
		}
		
		Postagem postagem = postagemService.buscarPorId(postagemId);
		
		if(postagem.notContains(arquivo)) {
			throw new EntidadeInexistenteException(ArquivoErrors.FORBIDDEN_REMOVER_BY_INVALID_POSTAGEM);
		}
		
		postagem.remove(arquivo);
		
		preencherCamposAuditoria(postagem, usuario);
	}
	
}
