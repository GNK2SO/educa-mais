package br.com.projeto.educamais.controller.usuario.form;

import static br.com.projeto.educamais.util.Util.criptografar;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import br.com.projeto.educamais.domain.Usuario;

public class UsuarioForm {
	
	@NotNull @NotEmpty @Length(max = 16)
	public String nome;
	
	@NotNull @NotEmpty @Length(max = 32)
	public String email;
	
	@NotNull @NotEmpty @Length(min = 8, max = 16)
	public String senha;
	
	public Usuario toUsuario() 
	{
		Usuario usuario = new Usuario();
		
		usuario.setNome(this.getNome());
		usuario.setEmail(this.getEmail());
		usuario.setSenha(criptografar(this.getSenha()));
		
		return usuario;
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
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
}
