package com.example.algamoney.api.exceptionhandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * 
 * @author Rodrigo
 * Classe para reescrita das exceções lançadas
 * 
 * @ControllerAdvice --> Controlador que observa a aplicação como um todo
 */
@ControllerAdvice
public class AlgamoneyExceptionHandler extends ResponseEntityExceptionHandler {
	
	/* Atributo que disponibiliza as mensagens cadastradas no messages.properties */
	@Autowired
	private MessageSource mensagensCadastradas;
	
	/**
	 * Método que captura a exceção que é lançada quando o recurso é chamado com propriedades desconhecidas
	 */
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException erro, HttpHeaders headers, HttpStatus status, WebRequest request) {
		String mensagem = mensagensCadastradas.getMessage("mensagem.invalida", null, LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = erro.getCause().toString();
		List<Erro> erros = Arrays.asList(new Erro(mensagem, mensagemDesenvolvedor));
		return handleExceptionInternal(erro, erros, headers, HttpStatus.BAD_REQUEST, request);
	}
	
	/**
	 * Método que captura exceções lançadas por intermédio do bean validation
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException erro, HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<Erro> erros = criarListaDeErros(erro.getBindingResult());
		return handleExceptionInternal(erro, erros, headers, HttpStatus.BAD_REQUEST, request);
	}
	
	/**
	 * Recepciona os erros gerados pela validação do bean validation e devolve uma lista dos erros
	 * @param bindingResult
	 * @return
	 */
	private List<Erro> criarListaDeErros(BindingResult bindingResult) {
		List<Erro> erros = new ArrayList<Erro>();
		
		for(FieldError fieldError : bindingResult.getFieldErrors()) {
			String mensagem = mensagensCadastradas.getMessage(fieldError, LocaleContextHolder.getLocale());;
			String mensagemDesenvolvedor = fieldError.toString();
			erros.add(new Erro(mensagem, mensagemDesenvolvedor));
		}
		
		return erros;
	}
	
	/*
	 * Classe exclusiva para encaminhar mensagens de erro ao usuário e desenvolvedor
	 * 
	 * */
	public static class Erro {
		
		private String mensagem;
		private String mensagemDesenvolvedor;
		
		public Erro(String mensagem, String mensagemDesenvolvedor) {
			this.mensagem = mensagem;
			this.mensagemDesenvolvedor = mensagemDesenvolvedor;
		}

		public String getMensagem() {
			return mensagem;
		}

		public void setMensagem(String mensagem) {
			this.mensagem = mensagem;
		}

		public String getMensagemDesenvolvedor() {
			return mensagemDesenvolvedor;
		}

		public void setMensagemDesenvolvedor(String mensagemDesenvolvedor) {
			this.mensagemDesenvolvedor = mensagemDesenvolvedor;
		}
	}
}
