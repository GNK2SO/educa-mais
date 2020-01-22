package br.com.projeto.educamais.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.projeto.educamais.domain.Arquivo;
import br.com.projeto.educamais.domain.Postagem;
import br.com.projeto.educamais.domain.Usuario;
import br.com.projeto.educamais.repository.ArquivoRepository;

@Service
public class ArquivoService {

	@Autowired
	private PostagemService service;
	
	@Autowired
	private ArquivoRepository repository;
	
	
	@Transactional
	public void salvar(Long postagemId, List<Arquivo> arquivos, Usuario usuario) {
		Postagem postagem = service.obterPorId(postagemId);
		
		for (Arquivo arquivo : arquivos) {
			Arquivo arquivoSalvo = repository.saveAndFlush(arquivo);
			postagem.add(arquivoSalvo);
		}
		
		service.atualizarPostagem(postagem, usuario);
	}
}
