package br.com.projeto.educamais.controller.postagem;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.projeto.educamais.controller.postagem.dto.PostagemDTO;
import br.com.projeto.educamais.controller.postagem.form.AtualizarPostagemForm;
import br.com.projeto.educamais.controller.postagem.form.PostagemForm;
import br.com.projeto.educamais.controller.turma.dto.TurmaPostagemDTO;
import br.com.projeto.educamais.domain.Postagem;
import br.com.projeto.educamais.domain.Turma;
import br.com.projeto.educamais.domain.Usuario;
import br.com.projeto.educamais.service.interfaces.PostagemService;
import br.com.projeto.educamais.service.interfaces.TurmaService;
import br.com.projeto.educamais.util.HttpStatusCode;
import br.com.projeto.educamais.util.Util;
import br.com.projeto.educamais.util.messages.PostagemErrors;
import br.com.projeto.educamais.util.messages.TurmaErrors;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/educamais/turmas")
public class PostagemController {

	@Autowired
	private PostagemService postagemService;

	@Autowired
	private TurmaService turmaService;
	
	@GetMapping("/{turmaId}/postagens")
	@ApiOperation(value = "Obter todas as postagens cadastradas que pertence a turma de id igual à {turmaId}.")
	@ApiResponses({
		@ApiResponse(code = HttpStatusCode.FORBIDDEN, message = TurmaErrors.FORBIDDEN_NOT_PARTICIPATE),
		@ApiResponse(code = HttpStatusCode.NOT_FOUND, message = TurmaErrors.NOT_FOUND)
	})
	public ResponseEntity<TurmaPostagemDTO> obterPostagensTurma(@PathVariable Long turmaId, @RequestParam(value = "page", defaultValue = "0") int pageNumber, Principal principal) {
		
		Usuario usuario = Util.recuperarUsuarioLogado(principal);
		Turma turma = turmaService.buscarTurmaPorId(turmaId);
		List<Postagem> postagens = postagemService.buscarPostagensPageablePorTurma(turma, pageNumber, usuario);
		
		return ResponseEntity.status(HttpStatus.OK).body(new TurmaPostagemDTO(turma, postagens));
	}
	
	
	@PostMapping("/{turmaId}/postagens")
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(value = "Cadastrar uma postagem que pertence a turma de id igual à {turmaId}.")
	@ApiResponses({
		@ApiResponse(code = HttpStatusCode.FORBIDDEN, message = PostagemErrors.FORBIDDEN_SALVAR_POSTAGEM),
		@ApiResponse(code = HttpStatusCode.NOT_FOUND, message = TurmaErrors.NOT_FOUND)
	})
	public ResponseEntity<PostagemDTO> cadastrarPostagem(@RequestBody @Valid PostagemForm form, @PathVariable Long turmaId, Principal principal) {
		
		Usuario usuario = Util.recuperarUsuarioLogado(principal);
		Turma turma = turmaService.buscarTurmaPorId(turmaId);
		Postagem postagem = postagemService.salvar(turma, usuario, form.toPostagem());
		
		return ResponseEntity.status(HttpStatus.CREATED).body(new PostagemDTO(postagem));
	}
	
	@PutMapping("/{turmaId}/postagens/{postagemId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation(value = "Atualizar dados da postagem de id igual à {postagemId} que pertence a turma de id igual à {turmaId}.")
	@ApiResponses({
		@ApiResponse(code = HttpStatusCode.FORBIDDEN, message = PostagemErrors.FORBIDDEN_ATUALIZAR_POSTAGEM),
		@ApiResponse(code = HttpStatusCode.NOT_FOUND, message = TurmaErrors.NOT_FOUND + "\n" + PostagemErrors.NOT_FOUND)
	})
	public ResponseEntity<Void> atualizarPostagem(@RequestBody @Valid AtualizarPostagemForm form, @PathVariable Long turmaId, @PathVariable Long postagemId, Principal principal) {
		
		Usuario usuario = Util.recuperarUsuarioLogado(principal);
		Turma turma = turmaService.buscarTurmaPorId(turmaId);
		postagemService.atualizarPostagem(turma, usuario, form.getPostagem(postagemId));
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
	@DeleteMapping("/{turmaId}/postagens/{postagemId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation(value = "Deletar a postagem de id igual à {postagemId} que pertence a turma de id igual à {turmaId}.")
	@ApiResponses({
		@ApiResponse(code = HttpStatusCode.FORBIDDEN, message = PostagemErrors.FORBIDDEN_REMOVER_POSTAGEM),
		@ApiResponse(code = HttpStatusCode.NOT_FOUND, message = TurmaErrors.NOT_FOUND + "\n" + PostagemErrors.NOT_FOUND)
	})
	public ResponseEntity<Void> deletarPostagem(@PathVariable Long turmaId, @PathVariable Long postagemId, Principal principal) {
		
		Usuario usuario = Util.recuperarUsuarioLogado(principal);
		Turma turma = turmaService.buscarTurmaPorId(turmaId);
		postagemService.deletarPostagem(turma, usuario, postagemId);
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
