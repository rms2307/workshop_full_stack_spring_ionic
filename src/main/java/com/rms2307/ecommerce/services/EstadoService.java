package com.rms2307.ecommerce.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rms2307.ecommerce.domain.Estado;
import com.rms2307.ecommerce.repositories.EstadoRepository;

@Service
public class EstadoService {
	
	@Autowired
	private EstadoRepository repo;

	public List<Estado> findAll() {
		return repo.findAllByOrderByNome();
	}
	
}
