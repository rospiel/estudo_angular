package com.example.algamoney.api.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.example.algamoney.api.model.Pessoa;
import com.example.algamoney.api.repository.PessoaRepository;
import com.example.algamoney.api.repository.filter.PessoaFilter;

/**
 * Classe responsável pelas tratativas de service do model Pessoa
 * @author Rodrigo
 *
 */
@Service
public class PessoaService {
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	/**
	 * 
	 * @param codigo
	 * @param pessoa
	 * @return
	 * 
	 * Recepciona o codigo da pessoa que será atualizado e o model Pessoa com as novas informações
	 * para salvar no banco
	 * 
	 */
	public Pessoa atualizar(Long codigo, Pessoa pessoa) {
		Pessoa pessoaBanco = buscarPessoaPeloCodigo(codigo);
		
		BeanUtils.copyProperties(pessoa, pessoaBanco, "codigo");
		return pessoaRepository.save(pessoaBanco);
	}
	
	/**
	 * 
	 * @param codigo
	 * @param ativo
	 * 
	 * Método que atualiza o atributo ativo da pessoa
	 */
	public void atualizarAtributoAtivo(Long codigo, Boolean ativo) {
		Pessoa pessoaBanco = buscarPessoaPeloCodigo(codigo);
		pessoaBanco.setAtivo(ativo);
		pessoaRepository.save(pessoaBanco);
	}
	
	/**
	 * 
	 * @param codigo
	 * @return
	 * 
	 * Busca a pessoa no banco, caso não encontre lança código 404 do contrário cópia as informações novas 
	 * e devolve o model pessoa atualizado
	 */
	private Pessoa buscarPessoaPeloCodigo(Long codigo) {
		Pessoa pessoaBanco = pessoaRepository.findOne(codigo);
		
		if(pessoaBanco == null) {
			throw new EmptyResultDataAccessException(1);
		}
		return pessoaBanco;
	}
	
	/**
	 * 
	 * @param pessoaFilter
	 * @param pageable
	 * @return
	 * 
	 * Busca a pessoa conforme nome informado no filtro
	 */
	public Page<Pessoa> filtrar(PessoaFilter pessoaFilter, Pageable pageable) {
		StringBuilder nome = new StringBuilder("%");
		nome.append(pessoaFilter.getNome());
		nome.append("%");
		
		Page<Pessoa> pessoas = pessoaRepository.filtrar(nome.toString(), pageable);
		
		return pessoas;
	}
}
