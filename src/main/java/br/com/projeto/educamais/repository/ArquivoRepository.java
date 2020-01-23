package br.com.projeto.educamais.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.projeto.educamais.domain.Arquivo;

public interface ArquivoRepository extends JpaRepository<Arquivo, Long> {

	public Optional<Arquivo> findByTitulo(String nome);
}
