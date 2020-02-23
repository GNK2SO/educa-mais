package br.com.projeto.educamais.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import br.com.projeto.educamais.exception.EntidadeInexistenteException;
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
	
	@ManyToOne
	private Turma turma;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<Pergunta> perguntas;
	
	@OneToMany(fetch = FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	private List<Resposta> respostas;
	
	public double getNotaTotal() {
		return this.perguntas.stream().mapToDouble(pergunta -> pergunta.getNota()).sum();
	}
	
	public void diminuirTentativa() {
		this.tentativas--;
	}
	
	public boolean estaHabilitada() {
		return this.dataEntrega.isAfter(LocalDate.now()) && this.tentativas > 0;
	}
	
	public boolean naoEstaHabilitada() {
		return !( estaHabilitada() );
	}
	
	public boolean pertenceAo(Usuario usuario) {
		if(usuario == null) {
			return false;
		}
		return this.aluno.equals(usuario);
	}
	
	public boolean naoPertenceAo(Usuario usuario) {
		return !pertenceAo(usuario);
	}

	public void corrigir() {
		double notaAluno = this.respostas.stream().mapToDouble(resposta -> {
			
			Optional<Pergunta> perguntaOptional = obterPerguntaPor(resposta.getPerguntaId());
			
			boolean isNotPresent = !perguntaOptional.isPresent();
			
			if(isNotPresent) {
				throw new EntidadeInexistenteException("Pergunta não encontrada. A pergunta informada não está cadastrada.");
			}
			
			Pergunta pergunta = perguntaOptional.get();
			boolean isCorreta = pergunta.getAlternativaCorreta().getId() == resposta.getAlternativaId();
			
			if(isCorreta) {
				return pergunta.getNota();
			}
			
			return 0;
			
		}).sum();
		
		this.nota = notaAluno;
	}
	
	public Optional<Pergunta> obterPerguntaPor(Long id) {
		return this.perguntas.stream()
				.filter(pergunta -> pergunta.getId() == id)
				.findFirst();
	}

	public boolean turmaIsNotEqualsTo(Turma turma) {
		if(this.turma == null)
		{
			return false;
		}
		return this.turma.equals(turma);
	}
}
