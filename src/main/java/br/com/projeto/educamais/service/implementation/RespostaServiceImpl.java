package br.com.projeto.educamais.service.implementation;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.projeto.educamais.domain.Resposta;
import br.com.projeto.educamais.repository.RespostaRepository;
import br.com.projeto.educamais.service.interfaces.RespostaService;

@Service
public class RespostaServiceImpl implements RespostaService {

	@Autowired
	private RespostaRepository repository;
	
	@Transactional
	public List<Resposta> salvar(List<Resposta> respostas) {
		return respostas.stream()
				.map(resposta -> repository.saveAndFlush(resposta))
				.collect(Collectors.toList());
	}
}
