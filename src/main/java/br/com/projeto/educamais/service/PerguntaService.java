package br.com.projeto.educamais.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.projeto.educamais.domain.Pergunta;
import br.com.projeto.educamais.repository.PerguntaRepository;

@Service
public class PerguntaService {

	@Autowired
	private PerguntaRepository repository;
	
	public List<Pergunta> salvar(List<Pergunta> perguntas) {
		return perguntas.stream()
				.map(pergunta -> repository.saveAndFlush(pergunta))
				.collect(Collectors.toList());
	}
}
