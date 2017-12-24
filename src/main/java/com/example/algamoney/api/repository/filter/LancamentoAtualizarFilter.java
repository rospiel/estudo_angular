package com.example.algamoney.api.repository.filter;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import com.example.algamoney.api.model.TipoLancamento;

/**
 * Classe de modelo para atualização de lançamento
 * @author Rodrigo
 *
 */
public class LancamentoAtualizarFilter {

	private Long codigo;

	@NotNull
	private String descricao;

	@NotNull
	private LocalDate dataVencimento;

	private LocalDate dataPagamento;

	@NotNull
	private BigDecimal valor;

	private String observacao;

	@NotNull
	private TipoLancamento tipoLancamento;

	@NotNull
	private CategoriaLancamento categoria;

	@NotNull
	private PessoaLancamento pessoa;

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public LocalDate getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(LocalDate dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public LocalDate getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(LocalDate dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public TipoLancamento getTipoLancamento() {
		return tipoLancamento;
	}

	public void setTipoLancamento(TipoLancamento tipoLancamento) {
		this.tipoLancamento = tipoLancamento;
	}

	public CategoriaLancamento getCategoria() {
		return categoria;
	}

	public void setCategoria(CategoriaLancamento categoria) {
		this.categoria = categoria;
	}

	public PessoaLancamento getPessoa() {
		return pessoa;
	}

	public void setPessoa(PessoaLancamento pessoa) {
		this.pessoa = pessoa;
	}

	public class PessoaLancamento {
		private Long codigo;

		public Long getCodigo() {
			return codigo;
		}

		public void setCodigo(Long codigo) {
			this.codigo = codigo;
		}
	}

	public class CategoriaLancamento {
		private Long codigo;

		public Long getCodigo() {
			return codigo;
		}

		public void setCodigo(Long codigo) {
			this.codigo = codigo;
		}

	}

}
