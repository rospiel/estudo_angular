package com.example.algamoney.api.repository.pessoa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.algamoney.api.model.Pessoa;

/**
 * Interface para pesquisar por filtros e projeção
 * @author Rodrigo
 *
 */
public interface PessoaRepositoryQuery {
	
	public final String COUNTFILTROPORNOME = "select count(1) from algamoneyapi.pessoa where nome like :nome order by ?#{#pageable}";
	public final String SELECTFILTROPORNOME = "select * from algamoneyapi.pessoa where nome like :nome order by ?#{#pageable}";
	
	@Query(value = SELECTFILTROPORNOME, countQuery = COUNTFILTROPORNOME, nativeQuery = true)
	public Page<Pessoa> filtrar(@Param("nome") String nome, Pageable pageable);
	
}
