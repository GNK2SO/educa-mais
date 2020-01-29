package br.com.projeto.educamais.domain;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Atividade extends EntidadeAuditavel {

	private static final long serialVersionUID = 1L;
	
	@Column(length = 32)
	private String titulo;
	
	@ManyToOne
	private Usuario aluno;
	
	@Column
	private String codigo;
	
	@Column
	private double nota;
	
	@Column
	private LocalDate dataEntrega;
	
	@Column
	private int tentativas;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<Pergunta> perguntas;
	
	@OneToMany(fetch = FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	private List<Resposta> respostas;
	
	public double getNotaTotal() {
		return this.perguntas.stream().mapToDouble(pergunta -> pergunta.getNota()).sum();
	}
	
	public boolean estaHabilitada() {
		return this.dataEntrega.isAfter(LocalDate.now()) &&
				this.tentativas > 0;
	}
	
	public boolean pertenceAo(Usuario usuario) {
		if(usuario == null) {
			return false;
		}
		return this.aluno.equals(usuario);
	}
}
