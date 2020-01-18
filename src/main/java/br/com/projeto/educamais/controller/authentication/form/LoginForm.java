package br.com.projeto.educamais.controller.authentication.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;


public class LoginForm {

	@NotNull @NotEmpty @Length(max = 32)
	public String email;
	
	@NotNull @NotEmpty @Length(min = 8, max = 16)
	public String senha;

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
	
	public UsernamePasswordAuthenticationToken getUsuario() {
		return new UsernamePasswordAuthenticationToken(this.email, this.senha);
	}
}
