package com.rms2307.ecommerce.dto;

import java.io.Serializable;
import java.util.List;

public class ProdutoNewDTO implements Serializable {
	private static final long serialVersionUID = 1L; 

	private Integer id;
	private String nome;
	private Double preco;
	private List<Integer> categoria_id;
	
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

	public List<Integer> getCategoria_id() {
		return categoria_id;
	}

}
