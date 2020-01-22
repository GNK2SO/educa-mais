package br.com.projeto.educamais.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.projeto.educamais.domain.Arquivo;

public interface ArquivoRepository extends JpaRepository<Arquivo, Long> {

}
