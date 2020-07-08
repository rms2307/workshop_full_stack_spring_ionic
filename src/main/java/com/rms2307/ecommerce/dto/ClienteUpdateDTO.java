package com.rms2307.ecommerce.dto;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import com.rms2307.ecommerce.domain.Cliente;
import com.rms2307.ecommerce.services.validation.ClienteUpdate;

@ClienteUpdate //Anotação customizada
public class ClienteUpdateDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer id;
	
	@NotEmpty(message = "Campo Obrigatório")
	@Length(min = 1, max = 120, message = "Min. 1, Max. 120")
	private String nome;
	
	@NotEmpty(message = "Campo Obrigatório")
	@Email(message = "Email inválido")
	private String email;
	
	private String telefone;
	
	public ClienteUpdateDTO() {
	}

	public ClienteUpdateDTO(Cliente obj) {
		id = obj.getId();
		nome = obj.getNome();
		email = obj.getEmail();
		telefone = obj.getTelefone();
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	
	
}
