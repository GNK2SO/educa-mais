package br.com.projeto.educamais.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.projeto.educamais.domain.Arquivo;
import br.com.projeto.educamais.domain.Postagem;
import br.com.projeto.educamais.domain.Turma;
import br.com.projeto.educamais.domain.Usuario;
import br.com.projeto.educamais.exception.EntidadeExistenteException;
import br.com.projeto.educamais.exception.EntidadeInexistenteException;
import br.com.projeto.educamais.exception.ProfessorNaoPodeSerAlunoException;
import br.com.projeto.educamais.exception.UsuarioJaEstaNaTurmaException;
import br.com.projeto.educamais.exception.UsuarioNaoTemPermissaoParaEssaAtividadeException;
import br.com.projeto.educamais.repository.TurmaRepository;
import br.com.projeto.educamais.util.Storage;
import br.com.projeto.educamais.util.messages.TurmaErrors;

@Service
public class TurmaService extends GenericService {

	
	@Autowired
	private TurmaRepository turmaRepository;
	
	@Autowired
	private PostagemService postagemService;
	
	@Autowired
	private Storage storage;
	
	@Transactional
	public Turma salva(Turma turma) {
		
		if(turmaRepository.existsByNome(turma.getNome())) {
			throw new EntidadeExistenteException(TurmaErrors.CONFLICT);
		}
		
		if(turmaRepository.existsByCodigo(turma.getCodigo())) {
			String codigo = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
			turma.setCodigo(codigo); 
		}
		
		preencherCamposAuditoria(turma, turma.getProfessor());
		return turmaRepository.saveAndFlush(turma);
	}
	
	@Transactional
	public List<Turma> buscarTurmas(Usuario usuario) {
		return turmaRepository.findByProfessorOrAlunosContaining(usuario, usuario);
	}
	
	@Transactional
	public Turma buscarTurmaPorId(Long id) {
		Optional<Turma> turma = turmaRepository.findById(id);
		if(turma.isPresent()) {
			return turma.get();
		}
		throw new EntidadeInexistenteException(TurmaErrors.NOT_FOUND);
	}
	
	@Transactional
	public Turma buscarTurmaPorCodigo(String codigo) {
		Optional<Turma> turma = turmaRepository.findByCodigo(codigo);
		if(turma.isPresent()) {
			return turma.get();
		}
		throw new EntidadeInexistenteException(TurmaErrors.NOT_FOUND);
	}
	
	@Transactional
	public void atualizarDados(Turma turma) {
		preencherCamposAuditoria(turma, turma.getProfessor());
	}
	
	@Transactional
	public void alterarNome(Long turmaId, String novoNomeTurma, Usuario usuario) {
		
		Turma turma = buscarTurmaPorId(turmaId);
		turma.setNome(novoNomeTurma);
		
		if(turma.professorIsNotEqualTo(usuario)) {
			throw new UsuarioNaoTemPermissaoParaEssaAtividadeException(TurmaErrors.FORBIDDEN_ATUALIZAR_TURMA);
		}
		
		preencherCamposAuditoria(turma, turma.getProfessor());
	}

	@Transactional
	public void participar(Turma turma, Usuario usuario) {
		if(turma.getProfessor().getId() == usuario.getId()) {
			throw new ProfessorNaoPodeSerAlunoException();
		}
		
		if(turma.contains(usuario)) {
			throw new UsuarioJaEstaNaTurmaException();
		}
		
		turma.add(usuario);
		
		preencherCamposAuditoria(turma, usuario);
		
		turmaRepository.saveAndFlush(turma);
	}

	@Transactional
	public void sairTurma(Turma turma, Usuario usuario) {
		if(turma.notContains(usuario)) {
			throw new UsuarioNaoTemPermissaoParaEssaAtividadeException(TurmaErrors.FORBIDDEN_SAIR_TURMA);
		}
		
		turma.remove(usuario);
	}

	@Transactional
	public void deletar(Long turmaId, Usuario usuario) {
		
		Turma turma = buscarTurmaPorId(turmaId);
		
		if(turma.professorIsNotEqualTo(usuario)) {
			throw new UsuarioNaoTemPermissaoParaEssaAtividadeException(TurmaErrors.FORBIDDEN_REMOVER_TURMA);
		}
		
		List<Postagem> postagens = postagemService.buscarPorTurma(turma, usuario);
		List<Arquivo> arquivos = new ArrayList<Arquivo>();
		
		postagens.stream().forEach(postagem -> {
			arquivos.addAll(postagem.getArquivos());
		});
		
		arquivos.stream().forEach(arquivo->{
			storage.deletar(arquivo);
		});
		
		turma.removeAllAlunos();
		turmaRepository.delete(turma);
	}
}
