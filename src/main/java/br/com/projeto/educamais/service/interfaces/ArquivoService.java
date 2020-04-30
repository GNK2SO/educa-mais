package br.com.projeto.educamais.service.interfaces;

import java.util.List;

import br.com.projeto.educamais.domain.Arquivo;
import br.com.projeto.educamais.domain.Usuario;

public interface ArquivoService {

	public void salvar(Long turmaId, Long postagemId, List<Arquivo> arquivos, Usuario usuario);
	public Arquivo buscarPorId(Long arquivoId);
	public void deletar(Long turmaId, Long postagemId, Arquivo arquivo, Usuario usuario);
}
