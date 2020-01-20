package br.com.projeto.educamais.domain;

import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class Turma extends EntidadeAuditavel {

	private static final long serialVersionUID = 1L;
	
	@Column(length = 24)
	private String nome;
	
	@Column(length = 8)
	private String codigo = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
	
	@ManyToOne(fetch = FetchType.EAGER)
	private Usuario professor;
	
	@ManyToMany(fetch = FetchType.EAGER)
	private List<Usuario> alunos;
	
	@OneToMany(fetch = FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	private List<Postagem> postagens;
	
	public void add(Usuario aluno) {
		this.alunos.add(aluno);
	}
	
	public void add(Postagem postagem) {
		this.postagens.add(postagem);
	}
	
	public void remove(Usuario aluno) {
		this.alunos.remove(aluno);
	}
	
	public boolean contains(Usuario aluno) {
		return this.alunos.contains(aluno);
	}
	
	public boolean notContains(Usuario aluno) {
		return !this.alunos.contains(aluno);
	}
	
	public boolean professorIsEqualTo(Usuario usuario) {
		return this.professor.equals(usuario);
	}
	
	public boolean professorIsNotEqualTo(Usuario usuario) {
		return !this.professor.equals(usuario);
	}
}
