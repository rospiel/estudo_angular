package com.example.algamoney.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

@Configuration
@EnableAuthorizationServer
/**
 * Classe de configuração para acesso aos recursos
 * @author Rodrigo
 *
 */
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
	
	@Autowired
	private AuthenticationManager administraAutenticacao;
	
	/**
	 * Configurando as informações de acesso ao cliente
	 */
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory()
			   /* Login do client */
		       .withClient("angular")
		       /* Senha do client */
		       .secret("@ngul@0")
		       /* O que será permitido */
		       .scopes("read", "write")
		       /* Como será o método de acesso do usuário */
		       .authorizedGrantTypes("password")
		       /* Tempo de validade do servidor */
		       .accessTokenValiditySeconds(1800);
	}
	
	/**
	 * Configurando o comportamento do endpoint
	 * Local onde será guardado o token
	 * E a AuthenticationManager pra obtenção da ResourceServerConfig
	 */
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.tokenStore(tokenStore())
		         .authenticationManager(administraAutenticacao);
	}
	
	/**
	 * Tem a função de informar a localização dos tokens gerados
	 * @return
	 */
	@Bean
	public TokenStore tokenStore() {
		return new InMemoryTokenStore();
	}
}
