package com.example.algamoney.api.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.algamoney.api.model.Lancamento;
import com.example.algamoney.api.repository.lancamento.LancamentoRepositoryQuery;

/**
 * 
 * @author Rodrigo
 * Interface do modelo lançamento, extendemos do JpaRepository pra obter varias implementações prontas
 * devemos informar o modelo bem como o tipo de dado da primary key da tabela
 */
public interface LancamentoRepository extends JpaRepository<Lancamento, Long>, LancamentoRepositoryQuery {
	
	List<Lancamento> findByDataVencimentoLessThanEqualAndDataPagamentoIsNull(LocalDate data);

}
