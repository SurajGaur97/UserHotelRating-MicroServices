package com.lcwd.gateway.models;

import java.util.Collection;

import lombok.Data;

@Data
public class AuthResponse {
	
	private String userId;
	
	private String accessToken;
	
	private String refreshToken;
	
	private Long expireAt;
	
	private Collection<String> authorities;

}
