package com.rms2307.ecommerce.domain.enums;

public enum TipoProduto {

	PENDENTE(1, "KG"), QUITADO(2, "Unidade");

	private int cod;
	private String descricao;

	private TipoProduto(int cod, String descricao) {
		this.cod = cod;
		this.descricao = descricao;
	}

	public int getCod() {
		return cod;
	}

	public String getDescricao() {
		return descricao;
	}

	public static TipoProduto toEnum(Integer cod) {
		if (cod == null) {
			return null;
		}

		for (TipoProduto x : TipoProduto.values()) {
			if (cod.equals(x.getCod())) {
				return x;
			}
		}

		throw new IllegalArgumentException("Id Inválido: " + cod);
	}
}
