package com.example.algamoney.api.repository.lancamento;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import com.example.algamoney.api.dto.LancamentoEstatisticaCategoria;
import com.example.algamoney.api.dto.LancamentoEstatisticaDia;
import com.example.algamoney.api.dto.LancamentoEstatisticaPessoa;
import com.example.algamoney.api.model.Categoria_;
import com.example.algamoney.api.model.Lancamento;
import com.example.algamoney.api.model.Lancamento_;
import com.example.algamoney.api.model.Pessoa_;
import com.example.algamoney.api.repository.filter.LancamentoFilter;
import com.example.algamoney.api.repository.projection.ResumoLancamento;

/**
 * Classe responsável por implementar consultas novas para model lancamento
 * @author Rodrigo
 * @PersistenceContext --> Obtendo conexão ao banco de dados
 * 
 *
 */
public class LancamentoRepositoryImpl implements LancamentoRepositoryQuery {
	
	@PersistenceContext
	private EntityManager manager;
	
	/*
	 * Cria a consulta por filtros 
	 */
	@Override
	public Page<Lancamento> filtrar(LancamentoFilter lancamentoFilter, Pageable pageable) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		
		/* Criteria por meio do model que será retornado */
		CriteriaQuery<Lancamento> criteria = builder.createQuery(Lancamento.class);
		
		/* Obtenção das colunas da tabela por meio do model */
		Root<Lancamento> root = criteria.from(Lancamento.class);
		
		/* Criando as condições where */
		Predicate[] predicates = criarRestricoes(lancamentoFilter, builder, root);
		
		criteria.where(predicates);
		
		TypedQuery<Lancamento> query = manager.createQuery(criteria);
		adicionarRestricoesDePaginacao(query, pageable);
		
		return new PageImpl<>(query.getResultList(), pageable, total(lancamentoFilter));
	}
	
	/*
	 * Cria a consulta com filtros com um retorno resumido 
	 */
	@Override
	public Page<ResumoLancamento> resumir(LancamentoFilter lancamentoFilter, Pageable pageable) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<ResumoLancamento> criteria = builder.createQuery(ResumoLancamento.class);
		Root<Lancamento> root = criteria.from(Lancamento.class);
		
		/* Criando retorno por meio de outro model mais resumido de lançamento */
		criteria.select(builder.construct(ResumoLancamento.class, 
										  root.get(Lancamento_.codigo),
										  root.get(Lancamento_.descricao),
										  root.get(Lancamento_.dataVencimento),
										  root.get(Lancamento_.dataPagamento),
										  root.get(Lancamento_.valor),
										  root.get(Lancamento_.tipoLancamento),
										  root.get(Lancamento_.categoria).get(Categoria_.nome),
										  root.get(Lancamento_.pessoa).get(Pessoa_.nome)));
		
		Predicate[] predicates = criarRestricoes(lancamentoFilter, builder, root);
		
		criteria.where(predicates);
		
		TypedQuery<ResumoLancamento> query = manager.createQuery(criteria);
		adicionarRestricoesDePaginacao(query, pageable);
		
		return new PageImpl<>(query.getResultList(), pageable, total(lancamentoFilter));
	}
	
	/**
	 * 
	 * @param lancamentoFilter
	 * @return
	 * Efetua a consulta pra obtenção do count da qtde de registros afim de conduzir a paginação
	 * 
	 */
	private Long total(LancamentoFilter lancamentoFilter) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root<Lancamento> root = criteria.from(Lancamento.class);
		
		Predicate[] predicates = criarRestricoes(lancamentoFilter, builder, root);
		criteria.where(predicates);
		
		criteria.select(builder.count(root));
		
		return manager.createQuery(criteria).getSingleResult();
	}
	
	/**
	 * 
	 * @param query
	 * @param pageable
	 * Método responsável por configurar o primeiro registro da consulta e a qtde máxima de registros pro retorno
	 * 
	 */
	private void adicionarRestricoesDePaginacao(TypedQuery<?> query, Pageable pageable) {
		int paginaAtual = pageable.getPageNumber();
		int totalRegistrosPorPagina = pageable.getPageSize();
		
		/* 0 x 3 = 0 
		 * 1 x 3 = 3
		 * 2 x 3 = 6 */
		int primeiroRegistroDaPagina = paginaAtual * totalRegistrosPorPagina;
		
		query.setFirstResult(primeiroRegistroDaPagina);
		query.setMaxResults(totalRegistrosPorPagina);
	}

	/*
	 * Configura as condições do where, usando metamodel afim de que não seja necessário digitar os nomes das colunas
	 */
	private Predicate[] criarRestricoes(LancamentoFilter lancamentoFilter, CriteriaBuilder builder, Root<Lancamento> root) {
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		if(StringUtils.isEmpty(lancamentoFilter.getDescricao()) != true) {
			predicates.add(builder.like(builder.lower(root.get(Lancamento_.descricao)), "%" + lancamentoFilter.getDescricao().toLowerCase() + "%"));
		}
		
		if(StringUtils.isEmpty(lancamentoFilter.getDataVencimentoDe()) != true) {
			predicates.add(builder.greaterThanOrEqualTo(root.get(Lancamento_.dataVencimento), lancamentoFilter.getDataVencimentoDe()));
		}
		
		if(StringUtils.isEmpty(lancamentoFilter.getDataVencimentoAte()) != true) {
			predicates.add(builder.lessThanOrEqualTo(root.get(Lancamento_.dataVencimento), lancamentoFilter.getDataVencimentoAte()));
		}
		
		return predicates.toArray(new Predicate[predicates.size()]);
	}
	/*
	 * Retorna a soma total dado o mês informado agrupando por categoria
	 */
	public List<LancamentoEstatisticaCategoria> porCategoria(LocalDate mesReferencia) {
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
		
		CriteriaQuery<LancamentoEstatisticaCategoria> criteriaQuery = criteriaBuilder.createQuery(LancamentoEstatisticaCategoria.class);
		
		Root<Lancamento> entidade = criteriaQuery.from(Lancamento.class);
		
		criteriaQuery.select(criteriaBuilder.construct(LancamentoEstatisticaCategoria.class, 
				entidade.get(Lancamento_.categoria), 
				criteriaBuilder.sum(entidade.get(Lancamento_.valor))));
		
		LocalDate primeiroDia = mesReferencia.withDayOfMonth(1);
		LocalDate ultimoDia = mesReferencia.withDayOfMonth(mesReferencia.lengthOfMonth());
		
		criteriaQuery.where(
				criteriaBuilder.greaterThanOrEqualTo(entidade.get(Lancamento_.dataVencimento), primeiroDia),
				criteriaBuilder.lessThanOrEqualTo(entidade.get(Lancamento_.dataVencimento), ultimoDia));
		
		criteriaQuery.groupBy(entidade.get(Lancamento_.categoria));
		
		TypedQuery<LancamentoEstatisticaCategoria> tipoRetorno = manager.createQuery(criteriaQuery);
		return tipoRetorno.getResultList();
	}
	
	/*
	 * Retorna a soma total por dia do mês informado agrupando por tipo de lançamento e data
	 */
	public List<LancamentoEstatisticaDia> porDia(LocalDate mesReferencia) {
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
		
		CriteriaQuery<LancamentoEstatisticaDia> criteriaQuery = criteriaBuilder.createQuery(LancamentoEstatisticaDia.class);
		
		Root<Lancamento> entidade = criteriaQuery.from(Lancamento.class);
		
		criteriaQuery.select(criteriaBuilder.construct(LancamentoEstatisticaDia.class, 
				entidade.get(Lancamento_.tipoLancamento), 
				entidade.get(Lancamento_.dataVencimento),
				criteriaBuilder.sum(entidade.get(Lancamento_.valor))));
		
		LocalDate primeiroDia = mesReferencia.withDayOfMonth(1);
		LocalDate ultimoDia = mesReferencia.withDayOfMonth(mesReferencia.lengthOfMonth());
		
		criteriaQuery.where(
				criteriaBuilder.greaterThanOrEqualTo(entidade.get(Lancamento_.dataVencimento), primeiroDia),
				criteriaBuilder.lessThanOrEqualTo(entidade.get(Lancamento_.dataVencimento), ultimoDia));
		
		criteriaQuery.groupBy(entidade.get(Lancamento_.tipoLancamento), entidade.get(Lancamento_.dataVencimento));
		
		TypedQuery<LancamentoEstatisticaDia> tipoRetorno = manager.createQuery(criteriaQuery);
		return tipoRetorno.getResultList();
	}
	
	/*
	 * Retorna a soma total conforme range de datas e pessoa agrupando por tipo de lançamento e pessoa
	 */
	public List<LancamentoEstatisticaPessoa> porPessoa(LocalDate dtInicio, LocalDate dtFim) {
		CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
		
		CriteriaQuery<LancamentoEstatisticaPessoa> criteriaQuery = criteriaBuilder.createQuery(LancamentoEstatisticaPessoa.class);
		
		Root<Lancamento> entidade = criteriaQuery.from(Lancamento.class);
		
		criteriaQuery.select(criteriaBuilder.construct(LancamentoEstatisticaPessoa.class, 
				entidade.get(Lancamento_.tipoLancamento), 
				entidade.get(Lancamento_.pessoa),
				criteriaBuilder.sum(entidade.get(Lancamento_.valor))));
		
		criteriaQuery.where(
				criteriaBuilder.greaterThanOrEqualTo(entidade.get(Lancamento_.dataVencimento), dtInicio),
				criteriaBuilder.lessThanOrEqualTo(entidade.get(Lancamento_.dataVencimento), dtFim));
		
		criteriaQuery.groupBy(entidade.get(Lancamento_.tipoLancamento), entidade.get(Lancamento_.pessoa));
		
		TypedQuery<LancamentoEstatisticaPessoa> tipoRetorno = manager.createQuery(criteriaQuery);
		return tipoRetorno.getResultList();
	}
}
