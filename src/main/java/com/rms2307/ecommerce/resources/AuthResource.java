package com.rms2307.ecommerce.resources;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rms2307.ecommerce.dto.EmailDTO;
import com.rms2307.ecommerce.security.JWTUtil;
import com.rms2307.ecommerce.security.UserSS;
import com.rms2307.ecommerce.services.AuthService;
import com.rms2307.ecommerce.services.UserService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/auth")
public class AuthResource {

	@Autowired
	private JWTUtil jwtUtil;
	
	@Autowired
	private AuthService service;

	@PostMapping(value = "/refresh_token")
	public ResponseEntity<Void> refreshToken(HttpServletResponse response) {
		UserSS user = UserService.authenticated();
		String token = jwtUtil.generateToken(user.getUsername());
		response.addHeader("Authorization", "Bearer " + token);
		response.addHeader("access-control-expose-headers", "Authorization");
		return ResponseEntity.noContent().build();
	}
	
	@ApiOperation(value = "Esqueci a senha")
	@PostMapping(value = "/forgot")
	public ResponseEntity<Void> forgot(@Valid @RequestBody EmailDTO objDTO) {
		service.sendNewPassword(objDTO.getEmail());
		return ResponseEntity.noContent().build();
	}

}
