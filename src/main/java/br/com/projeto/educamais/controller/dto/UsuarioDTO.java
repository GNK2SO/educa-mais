package br.com.projeto.educamais.controller.dto;

import java.util.ArrayList;
import java.util.List;

import br.com.projeto.educamais.domain.Usuario;

public class UsuarioDTO {
	public Long id;
	public String nome;
	public String email;
	
	public void fromUsuario(Usuario usuario) {
		this.setId(usuario.getId());
		this.setNome(usuario.getNome());
		this.setEmail(usuario.getEmail());
	}
	
	public List<UsuarioDTO> fromUsuarios(List<Usuario> usuarios) {
		
		List<UsuarioDTO> usuariosDTO = new ArrayList<UsuarioDTO>();
		UsuarioDTO dto = new UsuarioDTO();
		
		for(Usuario u: usuarios) {
			
			dto.setId(u.getId());
			dto.setNome(u.getNome());
			dto.setEmail(u.getEmail());
			
			usuariosDTO.add(dto);
		}
		
		return usuariosDTO;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
}
