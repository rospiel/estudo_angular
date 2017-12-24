package com.example.algamoney.api.repository.filter;

import org.hibernate.validator.constraints.NotBlank;

/**
 * Classe de filtro do model pessoa
 * @author Rodrigo
 *
 */
public class PessoaFilter {
	
	@NotBlank
	private String nome;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

}
