package com.example.algamoney.api.service;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.example.algamoney.api.event.RecursoCriadoEvent;
import com.example.algamoney.api.model.Lancamento;
import com.example.algamoney.api.model.Pessoa;
import com.example.algamoney.api.repository.LancamentoRepository;
import com.example.algamoney.api.repository.PessoaRepository;
import com.example.algamoney.api.service.exception.PessoaInexistenteOuInativaException;

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
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	/**
	 * 
	 * @param lancamento
	 * @param response
	 * @return
	 * 
	 * Tem a ação de incluir um lançamento porém antes verifica a existência de uma pessoa cadastrada e ativa
	 * do contrário lança uma exceção
	 */
	public Lancamento criar(Lancamento lancamento, HttpServletResponse response) {
		Pessoa pessoaBanco = pessoaRepository.findOne(lancamento.getPessoa().getCodigo());
		
		if(pessoaBanco == null || pessoaBanco.isInativo()) {
			throw new PessoaInexistenteOuInativaException();
		}
		
		Lancamento lancamentoSalvo = lancamentoRepository.save(lancamento);
		
		publicarEvento.publishEvent(new RecursoCriadoEvent(this, response, lancamentoSalvo.getCodigo()));
		
		return lancamentoSalvo;
	}
}
