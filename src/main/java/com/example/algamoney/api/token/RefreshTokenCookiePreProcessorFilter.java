package com.example.algamoney.api.token;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.catalina.util.ParameterMap;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Filtro que sobrescreve a request afim de que seja disponibilizado o cookie refresh_token na requisição
 * para obtenção de um novo token de acesso ao recurso
 * @author Rodrigo
 *
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RefreshTokenCookiePreProcessorFilter implements Filter {

	
	/**
	 * Verifica se estamos lidando com a request do oauth e se temos a grat_type bem como a existência de cookies 
	 * na request, caso afirmativo recupera o cookie de acesso ao token de recurso de sobrescreve a request com
	 * o cookie de acesso 
	 * @param request
	 * @param response
	 * @param chain
	 * @throws IOException
	 * @throws ServletException
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		
		if("/oauth/token".equalsIgnoreCase(req.getRequestURI())   &&
		   "refresh_token".equals(req.getParameter("grant_type")) &&
		   req.getCookies() != null) {
				
			for(Cookie cookie : req.getCookies()) {
				if(cookie.getName().equals("refreshToken")) {
					String refreshToken = cookie.getValue();
					req = new MyServletRequestWrapper(req, refreshToken);
					break;
				}
			}
		}
		
		chain.doFilter(req, response);
		
	}
	
	
	@Override
	public void destroy() {}
	
	@Override
	public void init(FilterConfig arg0) throws ServletException {}
	
	/**
	 * Classe para substituição da request com o cookie pra obtenção do token de recurso
	 * @author Rodrigo
	 *
	 */
	static class MyServletRequestWrapper extends HttpServletRequestWrapper {
		
		private String refreshToken;
		
		public MyServletRequestWrapper(HttpServletRequest request, String refreshToken) {
			super(request);
			this.refreshToken = refreshToken;
		}
		
		/**
		 * Cria ParameterMap com os valores do atributo de mesmo nome da request que vamos 
		 * substituir e acrescenta o valor do cookie 
		 */
		@Override
		public Map<String, String[]> getParameterMap() {
			ParameterMap<String, String[]> map = new ParameterMap<String, String[]>(this.getRequest().getParameterMap());
			map.put("refresh_token", new String[] { this.refreshToken } );
			map.setLocked(true);
			return map;
		}
		
	}
	
}
