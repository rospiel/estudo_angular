package com.example.algamoney.api.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.algamoney.api.model.Lancamento;
import com.example.algamoney.api.repository.LancamentoRepository;

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
}
