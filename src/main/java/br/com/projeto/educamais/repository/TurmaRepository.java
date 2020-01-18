package br.com.projeto.educamais.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.projeto.educamais.domain.Turma;
import br.com.projeto.educamais.domain.Usuario;

@Repository
public interface TurmaRepository extends JpaRepository<Turma, Long> {

	public boolean existsByNome(String nome);
	
	public boolean existsByCodigo(String codigo);
	
	public List<Turma> findByProfessor(Usuario professor);
	
	public Optional<Turma> findByCodigo(String codigo);
	
}
