package br.com.projeto.educamais.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

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
public class Postagem extends EntidadeAuditavel{

	private static final long serialVersionUID = 1L;

	@Column(length = 32)
	private String titulo;
	
	@Column(length = 512)
	private String descricao;
	
	@ManyToOne
	private Turma turma;
	
	@OneToMany(fetch = FetchType.EAGER)
	private List<Arquivo> arquivos;
	
	public void add(Arquivo arquivo) {
		this.arquivos.add(arquivo);
	}
	public void remove(Arquivo arquivo) {
		this.arquivos.remove(arquivo);
	}
	public boolean notContains(Arquivo arquivo) {
		return !( this.arquivos.contains(arquivo) );
	}
}
