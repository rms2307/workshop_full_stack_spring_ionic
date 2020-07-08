package com.rms2307.ecommerce.repositories;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rms2307.ecommerce.domain.Cliente;
import com.rms2307.ecommerce.domain.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

	@Transactional
	Page<Pedido> findByCliente(Cliente cliente, Pageable pageRequest);
	
	@Transactional
	List<Pedido> findByCliente(Cliente cliente);

}
