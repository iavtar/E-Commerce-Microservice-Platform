package com.iavtar.authservice.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.iavtar.authservice.service.UserServiceImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JWTAuthenticationFilter extends OncePerRequestFilter {

	@Value("${app.jwt.header_string}")
	private String headerString;

	@Value("${app.jwt.token.prefix}")
	private String tokenPrefix;

	@Autowired
	private JWTTokenProvider tokenProvider;

	@Autowired
	private UserServiceImpl userService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		try {
			String jwt = getJWTFromRequest(request);
			if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
				String username = tokenProvider.getUsernameFromJWT(jwt);
				UserDetails userDetails = userService.loadUserByUsername(username);
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		} catch (Exception e) {
			log.error("Could not set user authentication in security context", e);
		}

		filterChain.doFilter(request, response);

	}

	private String getJWTFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader(headerString);
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(tokenPrefix)) {
			return bearerToken.substring(7, bearerToken.length());
		}
		return null;
	}

}
