package br.com.projeto.educamais.service.interfaces;

import java.util.List;
import br.com.projeto.educamais.domain.Turma;
import br.com.projeto.educamais.domain.Usuario;

public interface TurmaService {
	public Turma salva(Turma turma);
	public List<Turma> buscarTurmas(Usuario usuario);
	public Turma buscarTurmaPorId(Long id);
	public Turma buscarTurmaPorCodigo(String codigo);
	public void atualizarDados(Turma turma);
	public void alterarNome(Long turmaId, String novoNomeTurma, Usuario usuario);
	public void participar(Turma turma, Usuario usuario);
	public void sairTurma(Turma turma, Usuario usuario);
	public void deletar(Long turmaId, Usuario usuario);
}
