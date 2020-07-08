package com.rms2307.ecommerce.dto;

import javax.validation.constraints.NotEmpty;

public class EnderecoNewDTO {
	private Integer id;
	@NotEmpty(message = "Campo Obrigatório")
	private String logradouro;
	@NotEmpty(message = "Campo Obrigatório")
	private String numero;
	@NotEmpty(message = "Campo Obrigatório")
	private String complemento;
	@NotEmpty(message = "Campo Obrigatório")
	private String bairro;
	@NotEmpty(message = "Campo Obrigatório")
	private String cep;
	private Integer cidade_id;
	private Integer cliente_id;
	
	public EnderecoNewDTO() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public Integer getCidade_id() {
		return cidade_id;
	}

	public void setCidade_id(Integer cidade_id) {
		this.cidade_id = cidade_id;
	}

	public Integer getCliente_id() {
		return cliente_id;
	}

	public void setCliente_id(Integer cliente_id) {
		this.cliente_id = cliente_id;
	}

}
