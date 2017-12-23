package com.example.algamoney.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.expression.OAuth2MethodSecurityExpressionHandler;

/**
 * Classe de configuração pra autenticação e segurando da api
 * @author Rodrigo
 * @EnableWebSecurity --> Indica classe de segurança
 * @EnableResourceServer --> classe de disponibilização de acesso 
 * @EnableGlobalMethodSecurity --> Validar se o usuário tem permissão pra acessar aquele recurso
 * WebSecurityConfigurerAdapter --> Extendemos pra obtenção de métodos prontos de segurança
 */
@Configuration
@EnableWebSecurity
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Profile("oauth-security")
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	/*
	 * Configurando usuário de acesso
	 */
	@Autowired
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		/* Passando a classe com o método de validação do usuário --> AppUserDetailsService e o encode de senha */
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}
	
	/*
	 * Configurando como a segurança trabalhará
	 */
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		/* Liberando o recurso sem autenticação */
		.antMatchers("/categorias").permitAll()
		/* Demais recursos necessitam de autenticação */
		.anyRequest().authenticated()
		.and()
		/* Sessões não terão estados  */
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
		/* Desabilitando ataques csrf */ 
		.csrf().disable();
		
	}
	
	/*
	 * Configurando o status do recurso 
	 */
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.stateless(true);
	}
	
	/*
	 * Informando qual o modelo de encode da senha
	 * */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	/*
	 * Devolvendo instância pra validar se o usuário tem acesso ao recurso
	 */
	@Bean
	public MethodSecurityExpressionHandler createExpressionHandler() {
		return new OAuth2MethodSecurityExpressionHandler();
	}
	
}
