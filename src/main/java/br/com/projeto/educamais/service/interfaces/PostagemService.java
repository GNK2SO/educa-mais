package br.com.projeto.educamais.service.interfaces;

import java.util.List;

import br.com.projeto.educamais.domain.Postagem;
import br.com.projeto.educamais.domain.Turma;
import br.com.projeto.educamais.domain.Usuario;

public interface PostagemService {
	
	public Postagem salvar(Turma turma, Usuario usuario, Postagem postagem);
	public Postagem buscarPorId(Long id);
	public List<Postagem> buscarPorTurma(Turma turma, Usuario usuario);
	public List<Postagem> buscarPostagensPageablePorTurma(Turma turma, int pageNumber, Usuario usuario);
	public void atualizarPostagem(Turma turma, Usuario usuario, Postagem postagem);
	public void deletarPostagem(Turma turma, Usuario usuario, Long postagemId);
}
