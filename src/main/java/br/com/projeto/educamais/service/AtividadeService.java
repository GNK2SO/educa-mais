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
import br.com.projeto.educamais.domain.Turma;
import br.com.projeto.educamais.domain.Usuario;
import br.com.projeto.educamais.exception.EntidadeInexistenteException;
import br.com.projeto.educamais.exception.UsuarioNaoTemPermissaoParaEssaAtividadeException;
import br.com.projeto.educamais.repository.AtividadeRepository;

@Service
public class AtividadeService extends GenericService {

	@Autowired
	private AtividadeRepository repository;
	
	@Autowired
	private TurmaService turmaService;
	
	@Autowired
	private PerguntaService perguntaService;
	
	@Transactional
	public Atividade salvar(Long turmaId, Usuario usuario, Atividade atividade, List<Long> idAlunos) {
		
		Turma turma = turmaService.buscarTurmaPorId(turmaId);
		
		if(turma.professorIsNotEqualTo(usuario)) {
			throw new UsuarioNaoTemPermissaoParaEssaAtividadeException("Apenas o professor tem permissão para cadastrar atividades.");
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
				
				throw new EntidadeInexistenteException("Aluno não encontrado. O aluno informado não está participando da turma.");
			}
			
		});
		
		return alunos;
	}
}
