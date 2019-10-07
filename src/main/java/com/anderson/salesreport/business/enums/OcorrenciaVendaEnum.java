package com.anderson.salesreport.business.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum OcorrenciaVendaEnum {
	VENDEDOR_INVALIDO("Venda inválida! Vendedor informado não foi encontrado! A venda será ignorada");

	private String mensagem;
}
