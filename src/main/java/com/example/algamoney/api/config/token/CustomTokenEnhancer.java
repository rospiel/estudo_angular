package com.example.algamoney.api.config.token;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import com.example.algamoney.api.security.UsuarioSistema;

/**
 * Classe que devolve um token personalizado, adicionado o nome do usuário logado na api 
 * @author Rodrigo
 *
 */
public class CustomTokenEnhancer implements TokenEnhancer {
	
	/**
	 * Método que adiciona o nome do usuário logado no token por meio da classe que extends de User
	 */
	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		/* Obtendo instância do usuario */
		UsuarioSistema usuarioSistema = (UsuarioSistema) authentication.getPrincipal();
		 
		/* Adicionando no map as informações que desejamos acrescentar no token */
		Map<String, Object> addInfo = new HashMap<String, Object>();
		addInfo.put("nome", usuarioSistema.getUsuario().getNome());
		
		/* Disponibilizando as informações */
		( (DefaultOAuth2AccessToken) accessToken ).setAdditionalInformation(addInfo);
		 
		return accessToken;
	}

}
