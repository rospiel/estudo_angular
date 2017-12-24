package com.example.algamoney.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.example.algamoney.api.model.Categoria;
import com.example.algamoney.api.repository.CategoriaRepository;

/**
 * Classe para tratativas de négocio do model categoria 
 * @author Rodrigo
 *
 */
@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	/**
	 * 
	 * @param codigo
	 * @return
	 * 
	 * Busca a categoria no banco, caso não encontre lança exceção de dado não encontrado
	 */
	public Categoria buscarCategoriaPeloCodigo(Long codigo) {
		Categoria categoria = categoriaRepository.findOne(codigo);
		
		if(categoria == null) {
			throw new EmptyResultDataAccessException(1);
		}
		return categoria;
	}
}
