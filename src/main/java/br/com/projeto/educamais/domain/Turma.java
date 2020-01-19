package br.com.projeto.educamais.domain;

import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class Turma extends EntidadeAuditavel {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(length = 24)
	private String nome;
	
	@Column(length = 8)
	private String codigo = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
	
	@ManyToOne(fetch = FetchType.EAGER)
	private Usuario professor;
	
	@ManyToMany(fetch = FetchType.EAGER)
	private List<Usuario> alunos;
	
	public void add(Usuario aluno) {
		this.alunos.add(aluno);
	}
	
	public boolean contains(Usuario aluno) {
		return this.alunos.contains(aluno);
	}
	
	public boolean professorIsEqualTo(Usuario usuario) {
		return this.professor.equals(usuario);
	}
	
	public boolean professorIsNotEqualTo(Usuario usuario) {
		return !this.professor.equals(usuario);
	}
}
