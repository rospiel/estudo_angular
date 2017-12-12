package com.example.algamoney.api.repository.lancamento;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.util.StringUtils;

import com.example.algamoney.api.model.Lancamento;
import com.example.algamoney.api.model.Lancamento_;
import com.example.algamoney.api.repository.filter.LancamentoFilter;

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
	public List<Lancamento> filtrar(LancamentoFilter lancamentoFilter) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		
		/* Criteria por meio do model que será retornado */
		CriteriaQuery<Lancamento> criteria = builder.createQuery(Lancamento.class);
		
		/* Obtenção das colunas da tabela por meio do model */
		Root<Lancamento> root = criteria.from(Lancamento.class);
		
		/* Criando as condições where */
		Predicate[] predicates = criarRestricoes(lancamentoFilter, builder, root);
		
		criteria.where(predicates);
		
		TypedQuery<Lancamento> query = manager.createQuery(criteria);
		
		return query.getResultList();
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

}
