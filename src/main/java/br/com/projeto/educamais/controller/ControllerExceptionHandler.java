package br.com.projeto.educamais.controller;

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

import br.com.projeto.educamais.controller.dto.ErroDTO;
import br.com.projeto.educamais.controller.dto.ErroFormularioDTO;
import br.com.projeto.educamais.exception.UsuarioExistenteException;

@RestControllerAdvice
public class ControllerExceptionHandler {

	@Autowired
	private MessageSource messageSource;
	
	@ResponseStatus(code = HttpStatus.CONFLICT)
    @ExceptionHandler(value = { UsuarioExistenteException.class })
    public ErroDTO handle(UsuarioExistenteException exception) {
    	return new ErroDTO("Conflito", 409, exception.getMessage(), "/educamais/usuario");
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

}