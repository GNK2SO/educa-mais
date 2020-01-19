package br.com.projeto.educamais.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.projeto.educamais.domain.Postagem;

public interface PostagemRepository extends JpaRepository<Postagem, Long> {

}
