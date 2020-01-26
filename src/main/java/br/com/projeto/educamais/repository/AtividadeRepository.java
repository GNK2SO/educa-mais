package br.com.projeto.educamais.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.projeto.educamais.domain.Atividade;

public interface AtividadeRepository extends JpaRepository<Atividade, Long> {
}
