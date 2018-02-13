package com.example.algamoney.api.resource;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.example.algamoney.api.event.RecursoCriadoEvent;
import com.example.algamoney.api.model.Pessoa;
import com.example.algamoney.api.repository.PessoaRepository;
import com.example.algamoney.api.repository.filter.PessoaFilter;
import com.example.algamoney.api.service.PessoaService;

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
	
	@Autowired
	private ApplicationEventPublisher publicarEvento;
	
	@Autowired
	private PessoaService pessoaService;
	
	/**
	 * Retorna a lista de pessoas 
	 */
	@GetMapping
	public Page<Pessoa> listar(Pageable pageable) {
		Page<Pessoa> pessoas = pessoaRepository.findAll(pageable);
		
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
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_PESSOA') and #oauth2.hasScope('read') ")
	public ResponseEntity<?> buscarPeloCodigo(@PathVariable Long codigo) {
		Pessoa pessoaEncontrada = pessoaRepository.findOne(codigo);
		
		return pessoaEncontrada != null ? ResponseEntity.ok(pessoaEncontrada) : ResponseEntity.notFound().build();
	}
	
	/**
	 * 
	 * @param pessoaFilter
	 * @param pageable
	 * @return
	 * 
	 * Retorna a lista de pessoas conforme nome passado  
	 */
	@GetMapping("/filtro")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_PESSOA') and #oauth2.hasScope('read') ")
	public Page<Pessoa> pesquisarPorNome(PessoaFilter pessoaFilter, Pageable pageable) {
		Page<Pessoa> pessoas = pessoaService.filtrar(pessoaFilter, pageable);
		
		return pessoas;
	}
	
	/**
	 * 
	 * @param pessoa
	 * @param response
	 * 
	 * Método que incluí no banco uma nova pessoa e devolve na resposta endereço do recurso e o objeto em questão
	 */
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_PESSOA') and #oauth2.hasScope('write') ")
	public ResponseEntity<Pessoa> criar(@Valid  @RequestBody Pessoa pessoa, HttpServletResponse response) {
		Pessoa pessoaSalva = pessoaRepository.save(pessoa);
		
		/* publicando o evento afim de que a localização do recurso recém criado seja disponibilizado no location do header */
		publicarEvento.publishEvent(new RecursoCriadoEvent(this, response, pessoaSalva.getCodigo()));
		
		return ResponseEntity.status(HttpStatus.CREATED).body(pessoaSalva);
	}
	
	/**
	 * 
	 * @param codigo
	 * 
	 * Método que remove um recurso e retorna o código 204 
	 */
	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('ROLE_REMOVER_PESSOA') and #oauth2.hasScope('write') ")
	public void excluir(@PathVariable Long codigo) {
		pessoaRepository.delete(codigo);
	}
	
	/**
	 * 
	 * @param codigo
	 * @param pessoa
	 * @return
	 * 
	 * Método que atualiza a pessoa e devolve o mesmo com dados atualizados
	 */
	@PutMapping("/{codigo}") 
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_PESSOA') and #oauth2.hasScope('write') ")
	public ResponseEntity<Pessoa> atualizar(@PathVariable Long codigo, @Valid @RequestBody Pessoa pessoa) {
		Pessoa pessoaAtualizada = pessoaService.atualizar(codigo, pessoa);
		
		return ResponseEntity.ok(pessoaAtualizada);
	}
	
	/**
	 * 
	 * @param codigo
	 * @param ativo
	 * 
	 * Método de atualização do atributo ativo da pessoa
	 */
	@PutMapping("/{codigo}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_PESSOA') and #oauth2.hasScope('write') ")
	public void atualizarAtributoAtivo(@PathVariable Long codigo, @RequestBody Boolean ativo) {
		pessoaService.atualizarAtributoAtivo(codigo, ativo);
	}
	
}
