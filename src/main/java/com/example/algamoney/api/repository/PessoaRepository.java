package com.example.algamoney.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.algamoney.api.model.Pessoa;
import com.example.algamoney.api.repository.pessoa.PessoaRepositoryQuery;

/**
 * 
 * @author Rodrigo
 * Interface do modelo pessoa, extendemos do JpaRepository pra obter varias implementações prontas
 * devemos informar o modelo bem como o tipo de dado da primary key da tabela
 */
public interface PessoaRepository extends JpaRepository<Pessoa, Long>, PessoaRepositoryQuery {

}
