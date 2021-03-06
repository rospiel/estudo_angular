package com.example.algamoney.api.resource;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.example.algamoney.api.event.RecursoCriadoEvent;
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
	
	@Autowired
	private ApplicationEventPublisher publicarEvento;
	
	/*
	 * Retorna a lista de categorias 
	 * @GetMapping --> chamado pelo verbo get
	 * ResponseEntity<?> --> como retorno nos casos em que é notFound e noContent 
	 * @PreAuthorize --> requer verificar se o usuário em questão tem acesso e se o cliente 
	 * tem o scope informado na AuthorizationServerConfig
	 */
	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_CATEGORIA') and #oauth2.hasScope('read')")
	public List<Categoria> listar() {
		List<Categoria> categorias = categoriaRepository.findAllByOrderByNomeAsc();
		
		/* return categorias.isEmpty() != true ? ResponseEntity.ok(categorias) : ResponseEntity.notFound().build(); 404 recurso não identificado */
		/* return categorias.isEmpty() != true ? ResponseEntity.ok(categorias) : ResponseEntity.noContent().build(); 204 busquei mas não tenho o que te mostrar */
		return categorias;
	}
	
	/**
	 * 
	 * @param categoria
	 * @param response
	 * 
	 * Método que incluí no banco uma nova categoria e devolve na resposta endereço do recurso e o objeto em questão
	 * @RequestBody --> Converte automaticamente pro objeto Categoria
	 * @ResponseStatus --> Indica o código http de retorno que enviaremos
	 * @Valid --> O objeto deve ser validado pelo bean validation
	 */
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_CATEGORIA') and #oauth2.hasScope('write') ")
	public ResponseEntity<Categoria> criar(@Valid @RequestBody Categoria categoria, HttpServletResponse response) {
		Categoria categoriaSalva = categoriaRepository.save(categoria);
		
		/* publicando o evento afim de que a localização do recurso recém criado seja disponibilizado no location do header */
		publicarEvento.publishEvent(new RecursoCriadoEvent(this, response, categoriaSalva.getCodigo()));
		
		return ResponseEntity.status(HttpStatus.CREATED).body(categoriaSalva);
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
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_CATEGORIA') and #oauth2.hasScope('read') ")
	public ResponseEntity<?> buscarPeloCodigo(@PathVariable Long codigo) {
		Categoria categoriaEncontrada = categoriaRepository.findOne(codigo);
		
		return categoriaEncontrada != null ? ResponseEntity.ok(categoriaEncontrada) : ResponseEntity.notFound().build(); 
	}
}
