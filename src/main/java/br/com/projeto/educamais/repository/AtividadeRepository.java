package br.com.projeto.educamais.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.projeto.educamais.domain.Atividade;
import br.com.projeto.educamais.domain.Turma;
import br.com.projeto.educamais.domain.Usuario;

public interface AtividadeRepository extends JpaRepository<Atividade, Long> {

	List<Atividade> findAllByTurma(Turma turma);

	List<Atividade> findAllByAluno(Usuario usuarioLogado);
}
