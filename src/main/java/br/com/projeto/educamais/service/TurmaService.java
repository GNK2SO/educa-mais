package br.com.projeto.educamais.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.projeto.educamais.domain.Arquivo;
import br.com.projeto.educamais.domain.Turma;
import br.com.projeto.educamais.domain.Usuario;
import br.com.projeto.educamais.exception.EntidadeExistenteException;
import br.com.projeto.educamais.exception.EntidadeInexistenteException;
import br.com.projeto.educamais.exception.ProfessorNaoPodeSerAlunoException;
import br.com.projeto.educamais.exception.UsuarioJaEstaNaTurmaException;
import br.com.projeto.educamais.exception.UsuarioNaoTemPermissaoParaEssaAtividadeException;
import br.com.projeto.educamais.repository.TurmaRepository;

@Service
public class TurmaService extends GenericService {

	
	@Autowired
	public TurmaRepository turmaRepository;
	
	@Transactional
	public Turma salva(Turma turma) {
		
		if(turmaRepository.existsByNome(turma.getNome())) {
			throw new EntidadeExistenteException("Falha ao cadastrar turma. Já existe uma turma cadastrada com esse nome.");
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
		throw new EntidadeInexistenteException("Falha ao obter turma. Turma não está cadastrada.");
	}
	
	@Transactional
	public Turma buscarTurmaPorCodigo(String codigo) {
		Optional<Turma> turma = turmaRepository.findByCodigo(codigo);
		if(turma.isPresent()) {
			return turma.get();
		}
		throw new EntidadeInexistenteException("Falha ao obter turma. Turma não está cadastrada.");
	}
	
	
	@Transactional
	public void atualizarDados(Turma turma) {
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
	}

	@Transactional
	public void sairTurma(Turma turma, Usuario usuario) {
		if(turma.notContains(usuario)) {
			throw new UsuarioNaoTemPermissaoParaEssaAtividadeException("Falha ao sair da turma. Usuário não participa dessa turma.");
		}
		
		turma.remove(usuario);
	}

	@Transactional
	public List<Arquivo> deletar(Turma turma) {
		List<Arquivo> arquivos = new ArrayList<Arquivo>();
		
		turma.getPostagens().stream().forEach(postagem -> {
			arquivos.addAll(postagem.getArquivos());
		});
		
		turma.removeAllAlunos();
		turmaRepository.delete(turma);
		
		return arquivos;
	}
}
