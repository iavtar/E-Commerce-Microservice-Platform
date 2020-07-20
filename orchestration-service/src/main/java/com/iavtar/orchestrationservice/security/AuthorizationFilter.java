package com.iavtar.orchestrationservice.security;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class AuthorizationFilter extends BasicAuthenticationFilter {


	Environment environment;

	public AuthorizationFilter(AuthenticationManager authenticationManager, Environment environment) {
		super(authenticationManager);
		this.environment = environment;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		String authorizationHeader = request.getHeader(environment.getProperty("authorization.tokenheadername"));

		if (authorizationHeader == null || !authorizationHeader.startsWith(environment.getProperty("authorization.tokenheaderprefix"))) {
			chain.doFilter(request, response);
			return;
		}

		UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		chain.doFilter(request, response);
	}

	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {

		String authorizationHeader = request.getHeader(environment.getProperty("authorization.tokenheadername"));

		if (authorizationHeader == null) {
			return null;
		}

		String token = authorizationHeader.replace(environment.getProperty("authorization.tokenheaderprefix"), "");

		Claims claims = Jwts.parser().setSigningKey(environment.getProperty("token.secret")).parseClaimsJws(token)
				.getBody();
		
		List<String> authArra = (List<String>) claims.get("authList");
		
		List<GrantedAuthority> authList = authArra.stream()
				.map(role -> new SimpleGrantedAuthority(role.toString())).collect(Collectors.toList());

		Object userId = claims.get("userId");

		return new UsernamePasswordAuthenticationToken(userId, null, authList);
	}

}
