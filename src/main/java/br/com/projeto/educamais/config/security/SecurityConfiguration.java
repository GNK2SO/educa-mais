package br.com.projeto.educamais.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.com.projeto.educamais.config.security.jwt.AutenticacaoViaToken;
import br.com.projeto.educamais.config.security.jwt.TokenService;
import br.com.projeto.educamais.service.AutenticacaoService;
import br.com.projeto.educamais.service.UsuarioService;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{
	
	@Autowired
	public AutenticacaoService authService;
	
	@Autowired
	public TokenService tokenService;
	
	@Autowired
	public UsuarioService usuarioService;
	
	private static final String[] AUTH_WHITELIST = { "/swagger-resources/**", "/swagger-ui.html", "/webjars/**", "/routes/**", "/favicon.ico" };

	
	@Override
	@Bean
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}

	//CONFIGURAÇÕES DE AUTENTICAÇÕES
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(authService)
			.passwordEncoder(new BCryptPasswordEncoder());
	}
	
	//CONFIGURAÇÕES DE AUTORIZAÇÃO
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.httpBasic().disable()
			.csrf().disable()
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and().addFilterBefore(new AutenticacaoViaToken(tokenService, usuarioService), UsernamePasswordAuthenticationFilter.class)
				.authorizeRequests()
				.antMatchers(HttpMethod.POST, "/auth").permitAll()
				.antMatchers(HttpMethod.POST, "/educamais/usuario").permitAll()
				.antMatchers(HttpMethod.GET, "/v2/api-docs").permitAll()
				.antMatchers(AUTH_WHITELIST).permitAll()
			.anyRequest().authenticated();
	}
	
	
	//CONFIGURAÇÕES DE RECURSOS ESTÁTICOS
	@Override
	public void configure(WebSecurity web) throws Exception {}
}
