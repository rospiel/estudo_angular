package com.example.algamoney.api.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Classe de implementação de profiles afim de que algumas configurações fiquem
 * dinâmicas para ambientes de desenvolvimento, homologação e produção
 * 
 * @author Rodrigo
 * @ConfigurationProperties --> Declarando que se trata de uma classe de
 *                          	configuração
 */
@ConfigurationProperties("algamoney")
public class AlgamoneyApiProperty {

	private String originPermitida = "http://localhost:4200";

	private final Seguranca seguranca = new Seguranca();
	
	private final Mail mail = new Mail();

	public String getOriginPermitida() {
		return originPermitida;
	}

	public void setOriginPermitida(String originPermitida) {
		this.originPermitida = originPermitida;
	}

	public Seguranca getSeguranca() {
		return seguranca;
	}
	
	public Mail getMail() {
		return mail;
	}

	public static class Seguranca {

		private boolean enableHttps;

		public boolean isEnableHttps() {
			return enableHttps;
		}

		public void setEnableHttps(boolean enableHttps) {
			this.enableHttps = enableHttps;
		}

	}

	public static class Mail {

		private String host;
		private Integer port;
		private String nomeUsuario;
		private String senha;

		public String getHost() {
			return host;
		}

		public void setHost(String host) {
			this.host = host;
		}

		public Integer getPort() {
			return port;
		}

		public void setPort(Integer port) {
			this.port = port;
		}

		public String getNomeUsuario() {
			return nomeUsuario;
		}

		public void setNomeUsuario(String nomeUsuario) {
			this.nomeUsuario = nomeUsuario;
		}

		public String getSenha() {
			return senha;
		}

		public void setSenha(String senha) {
			this.senha = senha;
		}

	}
}
