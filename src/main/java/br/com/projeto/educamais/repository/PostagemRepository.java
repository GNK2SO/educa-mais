package br.com.projeto.educamais.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.projeto.educamais.domain.Postagem;
import br.com.projeto.educamais.domain.Turma;

public interface PostagemRepository extends JpaRepository<Postagem, Long> {

	List<Postagem> findAllByTurma(Turma turma);

}
