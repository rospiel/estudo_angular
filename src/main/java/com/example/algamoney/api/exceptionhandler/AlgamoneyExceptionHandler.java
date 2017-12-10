package com.example.algamoney.api.exceptionhandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
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
		return handleExceptionInternal(erro, new Erro(mensagem, mensagemDesenvolvedor), headers, HttpStatus.BAD_REQUEST, request);
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
