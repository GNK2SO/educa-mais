package br.com.projeto.educamais.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.projeto.educamais.domain.Pergunta;

public interface PerguntaRepository extends JpaRepository<Pergunta, Long> {

}
