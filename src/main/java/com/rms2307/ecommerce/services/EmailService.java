package com.rms2307.ecommerce.services;

import org.springframework.mail.SimpleMailMessage;

import com.rms2307.ecommerce.domain.Pedido;

public interface EmailService {

	void sendOrderConfirmationEmail(Pedido obj);
	
	void sendEmail(SimpleMailMessage msg);
}
