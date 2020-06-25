package com.rms2307.ecommerce.dto;

import java.io.Serializable;

import com.rms2307.ecommerce.domain.Cidade;

public class CidadeDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String nome;
	private Integer estado_id;

	public CidadeDTO() {
	}
	
	public CidadeDTO(Cidade cidade) {
		id = cidade.getId();
		nome = cidade.getNome();
		estado_id = cidade.getEstado().getId();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Integer getEstado_id() {
		return estado_id;
	}

	public void setEstado_id(Integer estado_id) {
		this.estado_id = estado_id;
	}	
}