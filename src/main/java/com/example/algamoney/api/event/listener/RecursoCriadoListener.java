package com.example.algamoney.api.event.listener;

import java.net.URI;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.algamoney.api.event.RecursoCriadoEvent;

/**
 * 
 * @author Rodrigo
 * Classe responsável por escutar os eventos lançados e tomar ações
 */
@Component
public class RecursoCriadoListener implements ApplicationListener<RecursoCriadoEvent> {
	
	/**
	 * Método que recepciona a classe de evento lançada 
	 */
	@Override
	public void onApplicationEvent(RecursoCriadoEvent recursoCriadoEvent) {

		HttpServletResponse response = recursoCriadoEvent.getResponse();
		Long codigo = recursoCriadoEvent.getCodigo();
		
		adicionarHeaderLocation(response, codigo);
		
	}
	
	/**
	 * Método que disponibiliza no header a localização do recurso
	 * @param response
	 * @param codigo
	 */
	private void adicionarHeaderLocation(HttpServletResponse response, Long codigo) {
		/* Por meio da requisição atual construa a localização do recurso recém criado */
		URI endereco = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{codigo}").buildAndExpand(codigo).toUri();
		
		/* Cadastrando na resposta a localização do recurso */
		response.setHeader("Location", endereco.toASCIIString());
	}

}
