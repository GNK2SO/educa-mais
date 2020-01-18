package br.com.projeto.educamais.controller.usuario.dto;

import java.util.ArrayList;
import java.util.List;

import br.com.projeto.educamais.domain.Usuario;

public class UsuarioDTO {
	private Long id;
	private String nome;
	private String email;
	
	public UsuarioDTO() {}
	
	public UsuarioDTO(Usuario usuario) {
		this.setId(usuario.getId());
		this.setNome(usuario.getNome());
		this.setEmail(usuario.getEmail());
	}
	
	public List<UsuarioDTO> converter(List<Usuario> usuarios) {
		
		List<UsuarioDTO> usuariosDTO = new ArrayList<UsuarioDTO>();
		UsuarioDTO dto;
		
		for(Usuario usuario: usuarios) {
			
			dto = new UsuarioDTO(usuario);
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
