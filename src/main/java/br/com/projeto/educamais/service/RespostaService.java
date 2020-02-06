package br.com.projeto.educamais.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.projeto.educamais.domain.Resposta;
import br.com.projeto.educamais.repository.RespostaRepository;

@Service
public class RespostaService {

	@Autowired
	private RespostaRepository repository;
	
	@Transactional
	public List<Resposta> salvar(List<Resposta> respostas) {
		return respostas.stream()
				.map(resposta -> repository.saveAndFlush(resposta))
				.collect(Collectors.toList());
	}
}
