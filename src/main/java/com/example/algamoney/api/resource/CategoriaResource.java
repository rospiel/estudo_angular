package com.example.algamoney.api.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.algamoney.api.model.Categoria;
import com.example.algamoney.api.repository.CategoriaRepository;


/**
 * 
 * @author Rodrigo
 * Classe controladora de categorias
 * @RestController --> Informar que se trata de controlador tipo rest, logo as respostas serão convertidas pra json
 * @RequestMapping --> Localização do recurso
 */

@RestController
@RequestMapping("/categorias")
public class CategoriaResource {
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	/*
	 * Retorna a lista de categorias 
	 * @GetMapping --> chamado pelo verbo get
	 * ResponseEntity<?> --> como retorno nos casos em que é notFound e noContent 
	 */
	@GetMapping
	public List<Categoria> listar() {
		List<Categoria> categorias = categoriaRepository.findAll();
		
		/* return categorias.isEmpty() != true ? ResponseEntity.ok(categorias) : ResponseEntity.notFound().build(); 404 recurso não identificado */
		/* return categorias.isEmpty() != true ? ResponseEntity.ok(categorias) : ResponseEntity.noContent().build(); 204 busquei mas não tenho o que te mostrar */
		return categorias;
	}
}
