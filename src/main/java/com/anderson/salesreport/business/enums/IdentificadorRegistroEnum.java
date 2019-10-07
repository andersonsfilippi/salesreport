package com.anderson.salesreport.business.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum IdentificadorRegistroEnum {
	VENDEDOR("001"), CLIENTE("002"), VENDA("003");

	private String codigo;
}
