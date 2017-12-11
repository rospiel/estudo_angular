package com.example.algamoney.api.service;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.example.algamoney.api.event.RecursoCriadoEvent;
import com.example.algamoney.api.model.Lancamento;
import com.example.algamoney.api.repository.LancamentoRepository;

/**
 * Classe responsável pelas tratativas de service do model Lancamento
 * @author Rodrigo
 *
 */
@Service
public class LancamentoService {
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private ApplicationEventPublisher publicarEvento;
	
	/**
	 * 
	 * @param lancamento
	 * @param response
	 * @return
	 * 
	 * Tem a ação de incluir um lançamento
	 */
	public Lancamento criar(Lancamento lancamento, HttpServletResponse response) {
		Lancamento lancamentoSalvo = lancamentoRepository.save(lancamento);
		
		publicarEvento.publishEvent(new RecursoCriadoEvent(this, response, lancamentoSalvo.getCodigo()));
		
		return lancamentoSalvo;
	}
}
