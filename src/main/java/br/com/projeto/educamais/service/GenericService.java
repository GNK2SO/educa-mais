package br.com.projeto.educamais.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import br.com.projeto.educamais.domain.EntidadeAuditavel;
import br.com.projeto.educamais.domain.Usuario;

public abstract class GenericService {

	@Autowired
	public UsuarioService usuarioService;
	
	public void preencherCamposAuditoria(EntidadeAuditavel entity) {

		Usuario user = null;
		
		if (SecurityContextHolder.getContext().getAuthentication() != null) 
		{
			Object principal = SecurityContextHolder.getContext().getAuthentication().getName();
			
			if(principal instanceof Usuario) {
				System.out.println(((Usuario) principal).getEmail());
			}
			
		}


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
