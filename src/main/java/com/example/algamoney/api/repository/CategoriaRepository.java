package com.example.algamoney.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.algamoney.api.model.Categoria;

/**
 * 
 * @author Rodrigo
 * Interface do modelo categoria, extendemos do JpaRepository pra obter varias implementações prontas
 * devemos informar o modelo bem como o tipo de dado da primary key da tabela
 */
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
	
	List<Categoria> findAllByOrderByNomeAsc();

}
