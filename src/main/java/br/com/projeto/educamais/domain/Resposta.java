package br.com.projeto.educamais.domain;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import br.com.projeto.educamais.controller.atividade.form.RespostaForm;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Resposta {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column
	private Long perguntaId;
	
	@Column
	private Long alternativaId;
	
	public Resposta(Long perguntaId, Long alternativaId) {
		this.perguntaId = perguntaId;
		this.alternativaId = alternativaId;
	}
	
	public static List<Resposta> fromRespostaForm(List<RespostaForm> respostasForm) {
		return respostasForm.stream()
				.map((resposta) -> {
					return new Resposta(resposta.getPerguntaId(), resposta.getAlternativaId());
				}).collect(Collectors.toList());
	}


	
}
