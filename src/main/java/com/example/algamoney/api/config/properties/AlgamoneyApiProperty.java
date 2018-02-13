package com.example.algamoney.api.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Classe de implementação de profiles afim de que algumas configurações fiquem dinâmicas para 
 * ambientes de desenvolvimento, homologação e produção
 * @author Rodrigo
 * @ConfigurationProperties --> Declarando que se trata de uma classe de configuração
 */
@ConfigurationProperties("algamoney")
public class AlgamoneyApiProperty {

	private String originPermitida = "http://localhost:4200";

	private final Seguranca seguranca = new Seguranca();

	public String getOriginPermitida() {
		return originPermitida;
	}

	public void setOriginPermitida(String originPermitida) {
		this.originPermitida = originPermitida;
	}

	public Seguranca getSeguranca() {
		return seguranca;
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
}
