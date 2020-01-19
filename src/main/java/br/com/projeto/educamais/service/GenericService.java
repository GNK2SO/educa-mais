package br.com.projeto.educamais.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.projeto.educamais.domain.EntidadeAuditavel;
import br.com.projeto.educamais.domain.Usuario;

public abstract class GenericService {

	@Autowired
	public UsuarioService usuarioService;
	
	public void preencherCamposAuditoria(EntidadeAuditavel entity, Usuario usuario) {

		entity.setDataUltimaModificacao(LocalDate.now());

		if (entity.getId() == null) { // Entidade nova

			entity.setVersao(1L);
			entity.setDataCriacao(LocalDate.now());
			entity.setCriadoPor(usuario);

		} else { // Atualização de entidade

			entity.setUltimaModificacaoPor(usuario);
		}
	}
}
