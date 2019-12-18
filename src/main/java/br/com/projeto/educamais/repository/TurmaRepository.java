package br.com.projeto.educamais.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.projeto.educamais.domain.Turma;
import br.com.projeto.educamais.domain.Usuario;

@Repository
public interface TurmaRepository extends JpaRepository<Turma, Long> {

	public Turma findByNome(String nome);
	
	public Turma findByCodigo(String codigo);
	
	public List<Turma> findByProfessor(Usuario professor);
}
