package com.example.algamoney.api.resource;

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

import com.example.algamoney.api.model.Lancamento;
import com.example.algamoney.api.repository.LancamentoRepository;
import com.example.algamoney.api.service.LancamentoService;

/**
 * 
 * @author Rodrigo
 * Classe controladora de lancamento
 * @RestController --> Informar que se trata de controlador tipo rest, logo as respostas serão convertidas pra json
 * @RequestMapping --> Localização do recurso
 */

@RestController
@RequestMapping("/lancamentos")
public class LancamentoResource {
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private LancamentoService lancamentoService;
	
	/**
	 * Retorna a lista de lancamentos 
	 */
	@GetMapping
	public List<Lancamento> listar() {
		List<Lancamento> lancamentos = lancamentoRepository.findAll();
		
		return lancamentos;
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
		Lancamento lancamentoEncontrado = lancamentoRepository.findOne(codigo);
		
		return lancamentoEncontrado != null ? ResponseEntity.ok(lancamentoEncontrado) : ResponseEntity.notFound().build();
	}
	/**
	 * 
	 * @param lancamento
	 * @param response
	 * @return
	 * 
	 * Método que incluí no banco um novo lancamento e devolve na resposta o endereço do recurso e o objeto em questão
	 */
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Lancamento> criar(@Valid @RequestBody Lancamento lancamento, HttpServletResponse response) {
		Lancamento lancamentoSalvo = lancamentoService.criar(lancamento, response);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(lancamentoSalvo);
	}
}
