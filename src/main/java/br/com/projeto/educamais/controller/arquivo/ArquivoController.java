package br.com.projeto.educamais.controller.arquivo;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.projeto.educamais.domain.Arquivo;
import br.com.projeto.educamais.domain.Usuario;
import br.com.projeto.educamais.exception.FalhaUploadArquivoException;
import br.com.projeto.educamais.service.interfaces.ArquivoService;
import br.com.projeto.educamais.util.HttpStatusCode;
import br.com.projeto.educamais.util.Storage;
import br.com.projeto.educamais.util.Util;
import br.com.projeto.educamais.util.messages.ArquivoErrors;
import br.com.projeto.educamais.util.messages.PostagemErrors;
import br.com.projeto.educamais.util.messages.TurmaErrors;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/educamais/turmas")
public class ArquivoController {

	@Autowired
	private Storage storage;
	
	@Autowired
	private ArquivoService service;
	
	@PostMapping("/{turmaId}/postagens/{postagemId}/arquivo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation(value = "Upload de arquivos anexados ao formulário de uma postagem.")
	@ApiResponses({
		@ApiResponse(code = HttpStatusCode.BAD_REQUEST, message = FalhaUploadArquivoException.message),
		@ApiResponse(code = HttpStatusCode.NOT_FOUND, message = TurmaErrors.NOT_FOUND + "\n" + PostagemErrors.NOT_FOUND),
	})
	public ResponseEntity<Void> uploadArquivo(@RequestParam MultipartFile[] arquivos, @PathVariable Long turmaId, @PathVariable Long postagemId, Principal principal) {
		
		Usuario usuario = Util.recuperarUsuarioLogado(principal);
		List<Arquivo> arquivosSalvos = storage.upload(arquivos);
		service.salvar(turmaId, postagemId, arquivosSalvos, usuario);
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
	@DeleteMapping("/{turmaId}/postagens/{postagemId}/arquivo/{arquivoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation(value = "Remoção de arquivos anexados a uma postagem.")
	@ApiResponses({
		@ApiResponse(code = HttpStatusCode.NOT_FOUND, message = ArquivoErrors.NOT_FOUND + "\n" + TurmaErrors.NOT_FOUND + "\n" + PostagemErrors.NOT_FOUND),
		@ApiResponse(code = HttpStatusCode.FORBIDDEN, message = PostagemErrors.FORBIDDEN_REMOVER_POSTAGEM + "\n" + ArquivoErrors.FORBIDDEN_REMOVER_BY_INVALID_POSTAGEM),
	})
	public ResponseEntity<Void> deletarArquivos(@PathVariable Long arquivoId, @PathVariable Long turmaId, @PathVariable Long postagemId, Principal principal) {
		
		Usuario usuario = Util.recuperarUsuarioLogado(principal);
		Arquivo arquivo = service.buscarPorId(arquivoId);
		service.deletar(turmaId, postagemId, arquivo, usuario);
		storage.deletar(arquivo);
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
