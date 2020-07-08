package com.rms2307.ecommerce.dto;

import java.io.Serializable;

public class ProdutoNewDTO implements Serializable {
	private static final long serialVersionUID = 1L; 

	private Integer id;
	private String nome;
	private Integer categoria_id;
	
	public ProdutoNewDTO() {
		
	}
}
