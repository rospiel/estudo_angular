package com.example.algamoney.api.resource;

import java.net.URI;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
	
	/**
	 * 
	 * @param categoria
	 * @param response
	 * 
	 * Método que incluí no banco uma nova categoria e devolve na reposta endereço do recurso e o objeto em questão
	 * @RequestBody --> Converte automaticamente pro objeto Categoria
	 * @ResponseStatus --> Indica o código http de retorno que enviaremos
	 * @Valid --> O objeto deve ser validado pelo bean validation
	 */
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Categoria> criar(@Valid @RequestBody Categoria categoria, HttpServletResponse response) {
		Categoria categoriaSalva = categoriaRepository.save(categoria);
		
		/* Por meio da requisição atual construa a localização do recurso recém criado */
		URI endereco = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{codigo}").buildAndExpand(categoriaSalva.getCodigo()).toUri();
		
		/* Cadastrando na resposta a localização do recurso */
		response.setHeader("Location", endereco.toASCIIString());
		
		/* Devolvendo o json do recurso recém criado */
		return ResponseEntity.created(endereco).body(categoriaSalva);
	}
	
	/**
	 * 
	 * @param codigo
	 * @return
	 *  
	 * Método que busca um recurso pelo código, caso não encontre devolve 404
	 * @PathVariable --> Indica que o parâmetro código receberá o atributo de mesmo nome passado no endereço
	 */
	@GetMapping("/{codigo}")
	public ResponseEntity<?> buscarPeloCodigo(@PathVariable Long codigo) {
		Categoria categoriaEncontrada = categoriaRepository.findOne(codigo);
		
		return categoriaEncontrada != null ? ResponseEntity.ok(categoriaEncontrada) : ResponseEntity.notFound().build(); 
	}
}
