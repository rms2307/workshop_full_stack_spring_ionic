package com.rms2307.ecommerce.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rms2307.ecommerce.domain.Cidade;
import com.rms2307.ecommerce.repositories.CidadeRepository;

@Service
public class CidadeService {

	@Autowired
	private CidadeRepository repo;

	public List<Cidade> findAll(Integer estado_id) {
		return repo.findByEstado_idOrderByNome(estado_id);
	}

}
