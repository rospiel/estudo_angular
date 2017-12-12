package com.example.algamoney.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
 * Classe de configuração pra autenticação e segurando da api
 * @author Rodrigo
 * @EnableWebSecurity --> Indica classe de segurança
 * WebSecurityConfigurerAdapter --> Extendemos pra obtenção de métodos prontos de segurança
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	/*
	 * Configurando usuário em mémoria
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser("admin").password("admin").roles("ROLE");
	}
	
	/*
	 * Configurando como a segurança trabalhará
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		/* Liberando o recurso sem autenticação */
		.antMatchers("/categorias").permitAll()
		/* Demais recursos necessitam de autenticação */
		.anyRequest().authenticated()
		.and()
		/* Informando tipo de autenticação */
		.httpBasic()
		.and()
		/* Sessões não terão estados  */
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
		/* Desabilitando ataques csrf */ 
		.csrf().disable();
		
	}
	
}
