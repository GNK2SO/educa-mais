package br.com.projeto.educamais.service;

import java.time.LocalDate;

import br.com.projeto.educamais.domain.EntidadeAuditavel;
import br.com.projeto.educamais.domain.Usuario;

public abstract class GenericService {

	public void preencherCamposAuditoria(EntidadeAuditavel entity) {

		Usuario user = null;

		entity.setDataUltimaModificacao(LocalDate.now());

		if (entity.getId() == null) { // Entidade nova

			entity.setVersao(1L);
			entity.setDataCriacao(LocalDate.now());
			entity.setCriadoPor(user);

		} else { // Atualização de entidade

			entity.setVersao(entity.getVersao() + 1);
			entity.setUltimaModificacaoPor(user);
		}
	}
}
