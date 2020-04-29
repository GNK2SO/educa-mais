package br.com.projeto.educamais.service.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.projeto.educamais.domain.Atividade;
import br.com.projeto.educamais.domain.Pergunta;
import br.com.projeto.educamais.domain.Resposta;
import br.com.projeto.educamais.domain.Turma;
import br.com.projeto.educamais.domain.Usuario;
import br.com.projeto.educamais.exception.EntidadeInexistenteException;
import br.com.projeto.educamais.exception.UsuarioNaoTemPermissaoParaEssaAtividadeException;
import br.com.projeto.educamais.repository.AtividadeRepository;
import br.com.projeto.educamais.service.GenericService;
import br.com.projeto.educamais.service.RespostaService;
import br.com.projeto.educamais.service.interfaces.AtividadeService;
import br.com.projeto.educamais.service.interfaces.PerguntaService;
import br.com.projeto.educamais.util.messages.AtividadeErrors;
import br.com.projeto.educamais.util.messages.TurmaErrors;

@Service
public class AtividadeServiceImpl extends GenericService implements AtividadeService{

	@Autowired
	private AtividadeRepository repository;
	
	@Autowired
	private PerguntaService perguntaService;
	
	@Autowired
	private RespostaService respostaService;
	
	@Transactional
	public List<Atividade> buscarPorTurma(Turma turma, Usuario usuario) {
		if(turma.professorIsNotEqualTo(usuario) && turma.notContains(usuario)) {
			throw new UsuarioNaoTemPermissaoParaEssaAtividadeException("Usuário não participa turma.");
		}
		
		if(turma.professorIsEqualTo(usuario))
		{
			return repository.findAllByTurma(turma);
		}
		return repository.findAllByAluno(usuario);
	}
	
	@Transactional
	public Atividade buscarPorId(Long postagemId) {
		Optional<Atividade> atividade = repository.findById(postagemId);
		if(atividade.isPresent()) {
			return atividade.get();
		}
		throw new EntidadeInexistenteException(AtividadeErrors.NOT_FOUND);
	}
	
	@Transactional
	public Atividade salvar(Turma turma, Usuario usuario, Atividade atividade, List<Long> idAlunos) {
		
		if(turma.professorIsNotEqualTo(usuario)) {
			throw new UsuarioNaoTemPermissaoParaEssaAtividadeException(AtividadeErrors.FORBIDDEN_SALVAR_ATIVIDADE);
		}
		
		atividade.setTurma(turma);
		
		//TODO: REFATORAR POIS NÂO DEVE EXISTIR ATIVIDADES COM CÓDIGOS IGUAIS
		String codigo = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
		atividade.setCodigo(codigo);
		
		List<Pergunta> perguntasSalvas = perguntaService.salvar(atividade.getPerguntas());
		atividade.setPerguntas(perguntasSalvas);
		
		List<Usuario> alunos = getAlunosBy(idAlunos, turma);
		alunos.stream().forEach(aluno -> {
			atividade.setAluno(aluno);
			
			preencherCamposAuditoria(atividade, turma.getProfessor());
			repository.saveAndFlush(atividade);
		});
		
		return atividade;
	}
	
	private List<Usuario> getAlunosBy(List<Long> idAlunos, Turma turma) {
		List<Usuario> alunos = new ArrayList<Usuario>();

		idAlunos.stream().forEach(alunoId -> {
			
			Optional<Usuario> aluno = turma.getAlunoPor(alunoId);
			
			if(aluno.isPresent()) {
				alunos.add(aluno.get());
			} else {
				throw new EntidadeInexistenteException(TurmaErrors.FORBIDDEN_NOT_PARTICIPATE);
			}
			
		});
		
		return alunos;
	}

	@Transactional
	public void submeterRespostas(List<Resposta> respostas, Turma turma, Long atividadeId, Usuario usuario) {
		
		if(turma.professorIsNotEqualTo(usuario) && turma.notContains(usuario)) {
			throw new UsuarioNaoTemPermissaoParaEssaAtividadeException(TurmaErrors.FORBIDDEN_NOT_PARTICIPATE);
		}
		
		if(turma.professorIsEqualTo(usuario)) {
			throw new UsuarioNaoTemPermissaoParaEssaAtividadeException(AtividadeErrors.FORBIDDEN_PROFESSOR_SUBMIT_RESPOSTA);
		}
		
		Atividade atividade = buscarPorId(atividadeId);
		
		if(atividade.naoPertenceA(turma))
		{
			throw new EntidadeInexistenteException(AtividadeErrors.FORBIDDEN_ATIVIDADE_NOT_PERTENCE_TO_TURMA);
		}
		
		if(atividade.naoPertenceAo(usuario)) {
			throw new UsuarioNaoTemPermissaoParaEssaAtividadeException(AtividadeErrors.FORBIDDEN_ATIVIDADE_NOT_PERTENCE_TO_ALUNO);
		}
		
		if(atividade.naoEstaHabilitada()) {
			throw new UsuarioNaoTemPermissaoParaEssaAtividadeException(AtividadeErrors.FORBIDDEN_ATIVIDADE_DESABILITADA);
		}
		
		
		respostas = respostaService.salvar(respostas);
		
		atividade.diminuirTentativa();
		atividade.setRespostas(respostas);
		atividade.corrigir();
		
		repository.saveAndFlush(atividade);
	}
}
