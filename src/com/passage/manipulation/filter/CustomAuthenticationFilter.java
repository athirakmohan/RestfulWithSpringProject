package com.passage.manipulation.filter;

import java.io.IOException;
import java.text.MessageFormat;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.web.util.NestedServletException;



public class CustomAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	 private static final String SECURITY_TOKEN_KEY    = "token";
	 private static final String SECURITY_TOKEN_HEADER = "X-Token";
	 private String token = null;

	 protected CustomAuthenticationFilter() {
	   super("/");
	 }

	 @Override
	 public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
	   HttpServletRequest request = (HttpServletRequest) req;
	   HttpServletResponse response = (HttpServletResponse) res;

	   this.token = request.getParameter(SECURITY_TOKEN_KEY);
	  String token = request.getHeader("Authorization");
	  if(token != null && token.length() > 0){
	  String[] authenticationToken = token.split(" ");
	 
	   this.token=authenticationToken[1];
	  }
	 
	

	   Authentication authResult;
	   try {
	     authResult = attemptAuthentication(request, response);
	     if (authResult == null) {
	       return;
	     }
	   } catch (AuthenticationException failed) {
	     unsuccessfulAuthentication(request, response, failed);
	     return;
	   }

	   try {
	     successfulAuthentication(request, response, chain, authResult);
	   } catch (NestedServletException e) {
	     if(e.getCause() instanceof AccessDeniedException) {
	       unsuccessfulAuthentication(request, response, new LockedException("Forbidden"));
	     }
	   }
	 }

	 @Override
	 public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

	   AbstractAuthenticationToken userAuthenticationToken = authUserByToken(this.token);
	   if(userAuthenticationToken == null)
	     throw new AuthenticationServiceException(MessageFormat.format("Error | {0}", "Bad Token"));

	   return userAuthenticationToken;
	 }

	 private AbstractAuthenticationToken authUserByToken(String tokenRaw) {
	   AbstractAuthenticationToken authToken = null;
	   try {
	     
	     authToken = new UsernamePasswordAuthenticationToken("test", "test123", null);

	   } catch (Exception e) {
	     logger.error("Error during authUserByToken", e);
	   }
	   return authToken;
	 }

	 @Override
	 protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
	                                         Authentication authResult) throws IOException, ServletException {
	   SecurityContextHolder.getContext().setAuthentication(authResult);

	   getSuccessHandler().onAuthenticationSuccess(request, response, authResult);
	 }

	}
	
	
		
	

