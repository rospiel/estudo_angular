package com.example.algamoney.api.repository.lancamento;

import java.util.List;

import com.example.algamoney.api.model.Lancamento;
import com.example.algamoney.api.repository.filter.LancamentoFilter;

/**
 * Interface para pesquisar por filtros
 * @author Rodrigo
 *
 */
public interface LancamentoRepositoryQuery {
	
	public List<Lancamento> filtrar(LancamentoFilter lancamentoFilter);
	
}
