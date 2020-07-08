package com.rms2307.ecommerce.dto;

import java.io.Serializable;

public class ProdutoNewDTO implements Serializable {
	private static final long serialVersionUID = 1L; 

	private Integer id;
	private String nome;
	private Double preco;
	private Integer categoria_id;
	
	public ProdutoNewDTO() {
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

	public Double getPreco() {
		return preco;
	}

	public void setPreco(Double preco) {
		this.preco = preco;
	}

	public Integer getCategoria_id() {
		return categoria_id;
	}

	public void setCategoria_id(Integer categoria_id) {
		this.categoria_id = categoria_id;
	}
}
