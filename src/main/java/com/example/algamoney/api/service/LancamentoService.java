package com.example.algamoney.api.service;

import java.io.InputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.example.algamoney.api.dto.LancamentoEstatisticaPessoa;
import com.example.algamoney.api.event.RecursoCriadoEvent;
import com.example.algamoney.api.model.Categoria;
import com.example.algamoney.api.model.Lancamento;
import com.example.algamoney.api.model.Pessoa;
import com.example.algamoney.api.repository.LancamentoRepository;
import com.example.algamoney.api.repository.PessoaRepository;
import com.example.algamoney.api.repository.filter.LancamentoAtualizarFilter;
import com.example.algamoney.api.service.exception.PessoaInexistenteOuInativaException;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

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
	
	@Autowired
	private CategoriaService categoriaService;
	
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
		verificarPessoaDoLancamento(lancamento);
		
		Lancamento lancamentoSalvo = lancamentoRepository.save(lancamento);
		
		publicarEvento.publishEvent(new RecursoCriadoEvent(this, response, lancamentoSalvo.getCodigo()));
		
		return lancamentoSalvo;
	}

	
	/**
	 * 
	 * @param codigo
	 * @param lancamento
	 * @return
	 * 
	 * Atualiza o lancamento
	 * 
	 */
	public Lancamento atualizar(Long codigo, LancamentoAtualizarFilter lancamento) {
		Pessoa pessoaBanco = verificarPessoaDoLancamento(lancamento);
		Categoria categoriaBanco = verificarCategoriaDoLancamento(lancamento);
		Lancamento lancamentoBanco = buscarLancamentoPeloCodigo(codigo);
		BeanUtils.copyProperties(lancamento, lancamentoBanco, "codigo");
		lancamentoBanco.setCategoria(categoriaBanco);
		lancamentoBanco.setPessoa(pessoaBanco);
		return lancamentoRepository.save(lancamentoBanco);
	}
	
	/**
	 * 
	 * @param codigo
	 * @return
	 * 
	 * Busca o lancamento pelo codigo caso não encontre lança exceção de resultado vazio
	 */
	public Lancamento buscarLancamentoPeloCodigo(Long codigo) {
		Lancamento lancamentoBanco = lancamentoRepository.findOne(codigo);
		
		if(lancamentoBanco == null) {
			throw new EmptyResultDataAccessException(1);
		}
		
		return lancamentoBanco;
	}

	/**
	 * 
	 * @param codigo
	 * 
	 * Tem a ação de remover uma pessoa 
	 */
	public void excluir(Long codigo) {
		lancamentoRepository.delete(codigo);
	}
	
	/**
	 * 
	 * @param lancamento
	 * 
	 * Verifica a pessoa atrelado ao lancamento antes de salvar ou atualizar o mesmo
	 * Caso não encontre na base ou encontre pessoa inativa lança uma exceção
	 */
	private Pessoa verificarPessoaDoLancamento(LancamentoAtualizarFilter lancamento) {
		Pessoa pessoaBanco = pessoaRepository.findOne(lancamento.getPessoa().getCodigo());
		
		if(pessoaBanco == null || pessoaBanco.isInativo()) {
			throw new PessoaInexistenteOuInativaException();
		}
		
		return pessoaBanco;
	}
	
	/**
	 * 
	 * @param lancamento
	 * 
	 * Verifica a pessoa atrelado ao lancamento antes de salvar ou atualizar o mesmo
	 * Caso não encontre na base ou encontre pessoa inativa lança uma exceção
	 */
	private Pessoa verificarPessoaDoLancamento(Lancamento lancamento) {
		Pessoa pessoaBanco = pessoaRepository.findOne(lancamento.getPessoa().getCodigo());
		
		if(pessoaBanco == null || pessoaBanco.isInativo()) {
			throw new PessoaInexistenteOuInativaException();
		}
		
		return pessoaBanco;
	}
	
	/**
	 * 
	 * @param lancamento
	 * 
	 * Verifica a categoria atrelada ao lancamento antes de salvar ou atualizar o mesmo
	 * Caso não encontre na base a categoria lança uma exceção
	 */
	private Categoria verificarCategoriaDoLancamento(LancamentoAtualizarFilter lancamento) {
		return categoriaService.buscarCategoriaPeloCodigo(lancamento.getCategoria().getCodigo()); 
	}
	
	/**
	 * 
	 * @param dtInicio
	 * @param dtFim
	 * 
	 * Efetua a consulta dos totais por pessoa, faz uso do template em jasper convertendo em bytes 
	 * @return
	 * @throws JRException
	 */
	public byte[] relatorioPorPessoa(LocalDate dtInicio, LocalDate dtFim) throws JRException {
		List<LancamentoEstatisticaPessoa> dados = lancamentoRepository.porPessoa(dtInicio, dtFim);
		
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("DT_INICIO", Date.valueOf(dtInicio));
		parametros.put("DT_FIM", Date.valueOf(dtFim));
		parametros.put("REPORT_LOCALE", new Locale("pt", "BR"));
		
		InputStream inputStream = this.getClass().getResourceAsStream("/relatorios/lancamentos-por-pessoa.jasper");
		
		JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, parametros, new JRBeanCollectionDataSource(dados));
		
		return JasperExportManager.exportReportToPdf(jasperPrint);
	}
	
}
