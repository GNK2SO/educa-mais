package br.com.projeto.educamais.service.interfaces;

import java.util.List;


import br.com.projeto.educamais.domain.Atividade;
import br.com.projeto.educamais.domain.Resposta;
import br.com.projeto.educamais.domain.Turma;
import br.com.projeto.educamais.domain.Usuario;

public interface AtividadeService {

	public List<Atividade> buscarPorTurma(Turma turma, Usuario usuario);
	
	public Atividade buscarPorId(Long postagemId);
		
	public Atividade salvar(Turma turma, Usuario usuario, Atividade atividade, List<Long> idAlunos);

	public void submeterRespostas(List<Resposta> respostas, Turma turma, Long atividadeId, Usuario usuario);
}
