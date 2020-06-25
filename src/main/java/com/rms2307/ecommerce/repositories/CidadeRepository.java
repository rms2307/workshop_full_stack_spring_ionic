package com.rms2307.ecommerce.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.rms2307.ecommerce.domain.Cidade;

@Repository
public interface CidadeRepository extends JpaRepository<Cidade, Integer> {

	@Transactional(readOnly = true)
	public List<Cidade> findByEstado_idOrderByNome(Integer estado_id);
}

