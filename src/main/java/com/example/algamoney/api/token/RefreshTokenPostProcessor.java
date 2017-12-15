package com.example.algamoney.api.token;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * Classe controller para monitorar tokens refrese afim de que o mesmo seja disponibilizado como cookie e não mais no body 
 * requisição
 * @author Rodrigo
 * OAuth2AccessToken --> Monitore estas instâncias
 */
@ControllerAdvice
public class RefreshTokenPostProcessor implements ResponseBodyAdvice<OAuth2AccessToken>  {
	
	/*
	 * Método que verifica os métodos lançados, quando identificado o método que desejamos 
	 * interceptar devolve um true para que as ações sejam aplicadas  
	 */
	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return returnType.getMethod().getName().equals("postAccessToken");
	}

	/*
	 * Método que recepciona o id do tokenrefresh afim de que o mesmo seja removido do body mas adicionado 
	 * como cookie na request
	 * */
	@Override
	public OAuth2AccessToken beforeBodyWrite(OAuth2AccessToken body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
		HttpServletRequest req = ( (ServletServerHttpRequest) request ).getServletRequest();
		HttpServletResponse resp = ( (ServletServerHttpResponse) response ).getServletResponse();
		
		DefaultOAuth2AccessToken tokenBody = (DefaultOAuth2AccessToken) body; 
		String refreshToken = body.getRefreshToken().getValue();
		
		adicionarRefleshTokenNoCookie(refreshToken, req, resp);
		removerRefreshTokenDoBody(tokenBody);
		
		return body;
	}
	
	/*
	 * Método que remove o tokenrefresh do do body
	 */
	private void removerRefreshTokenDoBody(DefaultOAuth2AccessToken tokenBody) {
		tokenBody.setRefreshToken(null);
	}

	/*
	 * Método que adiciona o tokenrefresh no cookie
	 */
	private void adicionarRefleshTokenNoCookie(String refreshToken, HttpServletRequest req, HttpServletResponse resp) {
		Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
		refreshTokenCookie.setHttpOnly(true);
		refreshTokenCookie.setSecure(false);
		refreshTokenCookie.setPath(req.getContextPath() + "/oauth/token");
		refreshTokenCookie.setMaxAge(2592000);
		resp.addCookie(refreshTokenCookie);
		
	}

}
