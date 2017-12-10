package com.example.algamoney.api.service;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.example.algamoney.api.event.RecursoCriadoEvent;
import com.example.algamoney.api.model.Categoria;
import com.example.algamoney.api.model.Lancamento;
import com.example.algamoney.api.model.Pessoa;
import com.example.algamoney.api.repository.CategoriaRepository;
import com.example.algamoney.api.repository.LancamentoRepository;
import com.example.algamoney.api.repository.PessoaRepository;

/**
 * Classe responsável pelas tratativas de service do model Lancamento
 * @author Rodrigo
 *
 */
@Service
public class LancamentoService {
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
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
	 * Tem a ação de incluir um lançamento, porém antes verifica se a categoria e pessoa informadas existem
	 * do contrário lança exceções quanto a não existência destas informações no sistema
	 */
	public Lancamento criar(Lancamento lancamento, HttpServletResponse response) {
		StringBuilder mensagemErro = null;
		Categoria categoriaBanco = categoriaRepository.findOne(lancamento.getCategoria().getCodigo());
		
		if(categoriaBanco == null) {
			mensagemErro = new StringBuilder("Categoria de código " + lancamento.getCategoria().getCodigo() + " não encontrada.");
			throw new EmptyResultDataAccessException(mensagemErro.toString(), 1);
		}
		
		Pessoa pessoaBanco = pessoaRepository.findOne(lancamento.getPessoa().getCodigo());
		
		if(pessoaBanco == null) {
			mensagemErro = new StringBuilder("Pessoa de código " + lancamento.getPessoa().getCodigo() + " não encontrada.");
			throw new EmptyResultDataAccessException(mensagemErro.toString(), 1);
		}
		
		Lancamento lancamentoSalvo = lancamentoRepository.save(lancamento);
		
		publicarEvento.publishEvent(new RecursoCriadoEvent(this, response, lancamentoSalvo.getCodigo()));
		
		return lancamentoSalvo;
	}
}
