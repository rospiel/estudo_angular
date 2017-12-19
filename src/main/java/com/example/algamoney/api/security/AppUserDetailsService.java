package com.example.algamoney.api.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.algamoney.api.model.Usuario;
import com.example.algamoney.api.repository.UsuarioRepository;

/**
 * Classe responsável por disponibilizar o usuário do banco bem como suas permissões
 * @author Rodrigo
 * UserDetailsService --> Necessário para obtenção dos métodos previamente prontos
 */
@Service
public class AppUserDetailsService implements UserDetailsService {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	/*
	 * Método responsável por carregar o usuário e suas permissões
	 * */
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		/* Recuperando usuário do banco */
		Optional<Usuario> usuarioBanco = usuarioRepository.findByEmail(email);	
		
		/* Criando usuário, do contrário lança exceção do usuário inexistente */
		Usuario usuario = usuarioBanco.orElseThrow(() -> new UsernameNotFoundException("Usuário e/ou senha incorretos!"));
		
		/* Passando email de acesso, senha do usuário e permissões pra validar acesso */
		return new UsuarioSistema(usuario, getPermissoes(usuario));
	}
	
	/*
	 * Disponibiliza todas as permissões do usuário
	 * */
	private Collection<? extends GrantedAuthority> getPermissoes(Usuario usuario) {
		Set<SimpleGrantedAuthority> autorizacoes = new HashSet<SimpleGrantedAuthority>();
		usuario.getPermissoes().forEach(p -> autorizacoes.add(new SimpleGrantedAuthority(p.getDescricao().toUpperCase())));
		
		return autorizacoes;
	}

}
