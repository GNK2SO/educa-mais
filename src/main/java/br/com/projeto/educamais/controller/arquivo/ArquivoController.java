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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.projeto.educamais.domain.Arquivo;
import br.com.projeto.educamais.domain.Usuario;
import br.com.projeto.educamais.service.ArquivoService;
import br.com.projeto.educamais.util.Storage;
import br.com.projeto.educamais.util.Util;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/educamais/turmas")
public class ArquivoController {

	@Autowired
	private Storage storage;
	
	@Autowired
	private ArquivoService service;
	
	@PostMapping("/{turmaId}/postagens/{postagemId}/arquivo")
	@ApiOperation(value = "Upload de arquivos anexados ao formulário de uma postagem.")
	public ResponseEntity<Arquivo> uploadArquivo(@RequestParam MultipartFile[] arquivos, @PathVariable Long turmaId, @PathVariable Long postagemId, Principal principal) {
		
		
		Usuario usuario = Util.recuperarUsuarioLogado(principal);
		
		List<Arquivo> arquivosSalvos = storage.upload(arquivos);
		
		service.salvar(turmaId, postagemId, arquivosSalvos, usuario);
		
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	@DeleteMapping("/{turmaId}/postagens/{postagemId}/arquivo/{arquivoId}")
	@ApiOperation(value = "Remoção de arquivos anexados a uma postagem.")
	public ResponseEntity<Arquivo> deletarArquivos(@PathVariable Long arquivoId, @PathVariable Long turmaId, @PathVariable Long postagemId, Principal principal) {
		
		
		Usuario usuario = Util.recuperarUsuarioLogado(principal);
		
		Arquivo arquivo = service.buscarPorId(arquivoId);
		
		service.deletar(turmaId, postagemId, arquivo, usuario);
		
		storage.deletar(arquivo);
		
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
