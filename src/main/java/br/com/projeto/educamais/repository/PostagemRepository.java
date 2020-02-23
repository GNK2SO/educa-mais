package br.com.projeto.educamais.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.projeto.educamais.domain.Postagem;
import br.com.projeto.educamais.domain.Turma;

public interface PostagemRepository extends JpaRepository<Postagem, Long> {

	public List<Postagem> findAllByTurma(Turma turma);
	public Page<Postagem> findAllByTurma(Turma turma, Pageable page);

}
