package com.example.algamoney.api.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.example.algamoney.api.model.Pessoa;
import com.example.algamoney.api.repository.PessoaRepository;

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
	 * Busca a pessoa no banco, caso não encontre lança código 404 do contrário cópia as informações novas 
	 * e atualiza no banco
	 * 
	 */
	public Pessoa atualizar(Long codigo, Pessoa pessoa) {
		Pessoa pessoaBanco = pessoaRepository.findOne(codigo);
		
		if(pessoaBanco == null) {
			throw new EmptyResultDataAccessException(1);
		}
		
		BeanUtils.copyProperties(pessoa, pessoaBanco, "codigo");
		return pessoaRepository.save(pessoaBanco);
	}
}
