package com.rms2307.ecommerce.services;

import org.springframework.security.core.context.SecurityContextHolder;

import com.rms2307.ecommerce.security.UserSS;

public class UserService {

	// Retorna o usuario logado
	public static UserSS authenticated() {
		try {
			return (UserSS) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		} catch (Exception e) {
			return null;
		}
	}
}
