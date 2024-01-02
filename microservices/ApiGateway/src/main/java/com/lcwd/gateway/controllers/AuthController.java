package com.lcwd.gateway.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lcwd.gateway.models.AuthResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	Logger logger = LoggerFactory.getLogger(AuthController.class);
	
	/**
	 * @implNote It is the EndPoint for getting defined user information after successful login from OKTA login 
	 * page. And this EndPoint can be used for login to OKTA for users and it returns the token and user info for 
	 * further authorization process that help user to access all other API accessible through this token letter.    
	 * @param client
	 * @param user
	 * @param model
	 * @return
	 */
	@GetMapping("/login")
	public ResponseEntity<AuthResponse> login(
			@RegisteredOAuth2AuthorizedClient("okta") OAuth2AuthorizedClient client,
			@AuthenticationPrincipal OidcUser user,
			Model model)
	{
		logger.info("User email id: " + user.getEmail());
		
		AuthResponse authResponse = new AuthResponse();
		authResponse.setUserId(user.getEmail());
		authResponse.setAccessToken(client.getAccessToken().getTokenValue());
		authResponse.setRefreshToken(client.getRefreshToken().getTokenValue());
		authResponse.setExpireAt(client.getAccessToken().getExpiresAt().getEpochSecond());
		
		List<String> authorities = user.getAuthorities().stream().map(grantedauthority -> {
			return grantedauthority.getAuthority();
		}).collect(Collectors.toList());
		
		authResponse.setAuthorities(authorities);
		
		return	ResponseEntity.status(HttpStatus.OK).body(authResponse);	
	}

}
