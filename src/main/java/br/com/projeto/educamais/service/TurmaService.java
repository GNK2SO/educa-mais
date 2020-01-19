package br.com.projeto.educamais.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.projeto.educamais.domain.Turma;
import br.com.projeto.educamais.domain.Usuario;
import br.com.projeto.educamais.exception.EntidadeExistenteException;
import br.com.projeto.educamais.exception.EntidadeInexistenteException;
import br.com.projeto.educamais.exception.ProfessorNaoPodeSerAlunoException;
import br.com.projeto.educamais.exception.UsuarioJaEstaNaTurmaException;
import br.com.projeto.educamais.repository.TurmaRepository;

@Service
public class TurmaService extends GenericService {

	
	@Autowired
	public TurmaRepository turmaRepository;
	
	@Transactional
	public void salva(Turma turma) {
		
		if(turmaRepository.existsByNome(turma.getNome())) {
			throw new EntidadeExistenteException("Falha ao cadastrar turma. Já existe uma turma cadastrada com esse nome.");
		}
		
		if(turmaRepository.existsByCodigo(turma.getCodigo())) {
			String codigo = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
			turma.setCodigo(codigo); 
		}
		
		preencherCamposAuditoria(turma);
		turmaRepository.save(turma);
	}

	@Transactional
	public List<Turma> obterTodasTurmas() {
		return turmaRepository.findAll();
	}
	
	@Transactional
	public List<Turma> obterTurmasUsuarioAutenticado(Usuario usuario) {
		return turmaRepository.findByProfessorOrAlunosContaining(usuario, usuario);
	}
	
	@Transactional
	public Turma obterTurmaPorId(Long id) {
		Optional<Turma> turma = turmaRepository.findById(id);
		if(turma.isPresent()) {
			return turma.get();
		}
		throw new EntidadeInexistenteException("Falha ao obter turma. Turma não está cadastrada.");
	}
	
	@Transactional
	public Turma obterTurmaPorCodigo(String codigo) {
		Optional<Turma> turma = turmaRepository.findByCodigo(codigo);
		if(turma.isPresent()) {
			return turma.get();
		}
		throw new EntidadeInexistenteException("Falha ao obter turma. Turma não está cadastrada.");
	}
	
	
	@Transactional
	public Turma atualizarDados(Turma turma) {
		return turmaRepository.saveAndFlush(turma);
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
		turmaRepository.saveAndFlush(turma);
	}
}
