package br.com.projeto.educamais.controller.exception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.projeto.educamais.controller.exception.dto.ErroDTO;
import br.com.projeto.educamais.controller.exception.dto.ErroFormularioDTO;
import br.com.projeto.educamais.exception.AlunoNaoTemPermissaoParaEssaAtividadeException;
import br.com.projeto.educamais.exception.EntidadeExistenteException;
import br.com.projeto.educamais.exception.EntidadeInexistenteException;
import br.com.projeto.educamais.exception.ProfessorNaoPodeSerAlunoException;
import br.com.projeto.educamais.exception.UsuarioJaEstaNaTurmaException;
import br.com.projeto.educamais.exception.UsuarioNaoAutenticadoException;

@RestControllerAdvice
public class ControllerExceptionHandler {

	@Autowired
	private MessageSource messageSource;
	
	@ResponseStatus(code = HttpStatus.CONFLICT)
    @ExceptionHandler(value = { EntidadeExistenteException.class })
    public ErroDTO handle(EntidadeExistenteException exception) {
    	return new ErroDTO("Conflito", 409, exception.getMessage());
    }
	
	@ResponseStatus(code = HttpStatus.CONFLICT)
    @ExceptionHandler(value = { EntidadeInexistenteException.class })
    public ErroDTO handle(EntidadeInexistenteException exception) {
    	return new ErroDTO("Erro Requisição", 400, exception.getMessage());
    }
	
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = { MethodArgumentNotValidException.class })
    public List<ErroFormularioDTO> handle(MethodArgumentNotValidException exception) {
		
		List<ErroFormularioDTO> listaErroDTO = new ArrayList<>();
		
		List<FieldError> fieldsErrors = exception.getBindingResult().getFieldErrors();
		
		fieldsErrors.forEach(e -> {
			String erro = messageSource.getMessage(e, LocaleContextHolder.getLocale());
			ErroFormularioDTO erroDTO = new ErroFormularioDTO(e.getField(), erro);
			listaErroDTO.add(erroDTO);
		});
		
    	return listaErroDTO;
    }
	
	@ResponseStatus(code = HttpStatus.FORBIDDEN)
    @ExceptionHandler(value = { UsuarioNaoAutenticadoException.class })
    public ErroDTO handle(UsuarioNaoAutenticadoException exception) {
    	return new ErroDTO("Acesso Negado", 403, exception.getMessage());
    }
	
	@ResponseStatus(code = HttpStatus.FORBIDDEN)
    @ExceptionHandler(value = { ProfessorNaoPodeSerAlunoException.class })
    public ErroDTO handle(ProfessorNaoPodeSerAlunoException exception) {
    	return new ErroDTO("Acesso Negado", 403, exception.getMessage());
    }
	
	@ResponseStatus(code = HttpStatus.FORBIDDEN)
    @ExceptionHandler(value = { UsuarioJaEstaNaTurmaException.class })
    public ErroDTO handle(UsuarioJaEstaNaTurmaException exception) {
    	return new ErroDTO("Acesso Negado", 403, exception.getMessage());
    }

	@ResponseStatus(code = HttpStatus.FORBIDDEN)
    @ExceptionHandler(value = { AlunoNaoTemPermissaoParaEssaAtividadeException.class })
    public ErroDTO handle(AlunoNaoTemPermissaoParaEssaAtividadeException exception) {
    	return new ErroDTO("Acesso Negado", 403, exception.getMessage());
    }
	
}