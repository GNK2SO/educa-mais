package br.com.projeto.educamais.service;

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
import br.com.projeto.educamais.util.messages.AtividadeErrors;
import br.com.projeto.educamais.util.messages.TurmaErrors;

@Service
public class AtividadeService extends GenericService {

	@Autowired
	private AtividadeRepository repository;
	
	@Autowired
	private TurmaService turmaService;
	
	@Autowired
	private PerguntaService perguntaService;
	
	@Autowired
	private RespostaService respostaService;
	
	@Transactional
	public Turma buscarTurmaAtividades(Long turmaId, Usuario usuarioLogado) {
		Turma turma = turmaService.buscarTurmaPorId(turmaId);
		
		if(turma.professorIsNotEqualTo(usuarioLogado) && turma.notContains(usuarioLogado)) {
			throw new UsuarioNaoTemPermissaoParaEssaAtividadeException("Usuário não participa turma.");
		}
		return turma;
	}
	
	@Transactional
	public Atividade salvar(Long turmaId, Usuario usuario, Atividade atividade, List<Long> idAlunos) {
		
		Turma turma = turmaService.buscarTurmaPorId(turmaId);
		
		if(turma.professorIsNotEqualTo(usuario)) {
			throw new UsuarioNaoTemPermissaoParaEssaAtividadeException(AtividadeErrors.FORBIDDEN_SALVAR_ATIVIDADE);
		}
		
		List<Usuario> alunos = getAlunosBy(idAlunos, turma);
		
		String codigo = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
		atividade.setCodigo(codigo);
		
		List<Pergunta> perguntasSalvas = perguntaService.salvar(atividade.getPerguntas());
		atividade.setPerguntas(perguntasSalvas);
		
		alunos.stream().forEach(aluno -> {
			atividade.setAluno(aluno);
			
			preencherCamposAuditoria(atividade, turma.getProfessor());
			Atividade atividadeNova = repository.saveAndFlush(atividade);
			
			turma.add(atividadeNova);
			turmaService.atualizarDados(turma);
		});
		
		return atividade;
	}
	
	public List<Usuario> getAlunosBy(List<Long> idAlunos, Turma turma) {
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
	public void submeterRespostas(List<Resposta> respostas, Long turmaId, Long atividadeId, Usuario usuarioLogado) {
		Turma turma = turmaService.buscarTurmaPorId(turmaId);
		
		if(turma.professorIsNotEqualTo(usuarioLogado) && turma.notContains(usuarioLogado)) {
			throw new UsuarioNaoTemPermissaoParaEssaAtividadeException(TurmaErrors.FORBIDDEN_NOT_PARTICIPATE);
		}
		
		if(turma.professorIsEqualTo(usuarioLogado)) {
			throw new UsuarioNaoTemPermissaoParaEssaAtividadeException(AtividadeErrors.FORBIDDEN_PROFESSOR_SUBMIT_RESPOSTA);
		}
		
		Optional<Atividade> atividadeOptional = turma.getAtividadePor(atividadeId);
		
		if(atividadeOptional.isPresent()) {
			Atividade atividade = atividadeOptional.get();
			
			if(atividade.naoPertenceAo(usuarioLogado)) {
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
			
		} else {
			throw new EntidadeInexistenteException(AtividadeErrors.FORBIDDEN_ATIVIDADE_NOT_PERTENCE_TO_TURMA);
		}
		
	}
}
