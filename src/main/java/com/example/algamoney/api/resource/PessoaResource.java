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

import com.example.algamoney.api.model.Pessoa;
import com.example.algamoney.api.repository.PessoaRepository;

/**
 * 
 * @author Rodrigo
 * Classe controladora de pessoa
 * @RestController --> Informar que se trata de controlador tipo rest, logo as respostas serão convertidas pra json
 * @RequestMapping --> Localização do recurso
 */

@RestController
@RequestMapping("/pessoas")
public class PessoaResource {
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	/*
	 * Retorna a lista de pessoas 
	*/
	@GetMapping
	public List<Pessoa> listar() {
		List<Pessoa> pessoas = pessoaRepository.findAll();
		
		return pessoas;
	}
	
	/**
	 * 
	 * @param codigo
	 * @return
	 *  
	 * Método que busca um recurso pelo código, caso não encontre devolve 404
	 */
	@GetMapping("/{codigo}")
	public ResponseEntity<?> buscarPeloCodigo(@PathVariable Long codigo) {
		Pessoa pessoaEncontrada = pessoaRepository.findOne(codigo);
		
		return pessoaEncontrada != null ? ResponseEntity.ok(pessoaEncontrada) : ResponseEntity.notFound().build();
	}
	
	/**
	 * 
	 * @param pessoa
	 * @param response
	 * 
	 * Método que incluí no banco uma nova pessoa e devolve na reposta endereço do recurso e o objeto em questão
	 */
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Pessoa> criar(@Valid  @RequestBody Pessoa pessoa, HttpServletResponse response) {
		Pessoa pessoaSalva = pessoaRepository.save(pessoa);
		
		URI endereco = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{codigo}").buildAndExpand(pessoaSalva.getCodigo()).toUri();
		
		response.setHeader("Location", endereco.toASCIIString());
		
		return ResponseEntity.created(endereco).body(pessoaSalva);
	}
}
