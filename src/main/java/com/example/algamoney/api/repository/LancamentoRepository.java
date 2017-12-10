package com.example.algamoney.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.algamoney.api.model.Lancamento;

/**
 * 
 * @author Rodrigo
 * Interface do modelo lançamento, extendemos do JpaRepository pra obter varias implementações prontas
 * devemos informar o modelo bem como o tipo de dado da primary key da tabela
 */
public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

}
