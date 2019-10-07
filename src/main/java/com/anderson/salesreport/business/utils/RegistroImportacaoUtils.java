package com.anderson.salesreport.business.utils;

import org.apache.commons.lang3.StringUtils;

import com.anderson.salesreport.business.enums.IdentificadorRegistroEnum;

public final class RegistroImportacaoUtils {

	public static boolean isContentVendedor(String content) {
		return StringUtils.startsWith(content, IdentificadorRegistroEnum.VENDEDOR.getCodigo());
	}

	public static boolean isContentCliente(String content) {
		return StringUtils.startsWith(content, IdentificadorRegistroEnum.CLIENTE.getCodigo());
	}

	public static boolean isContentVenda(String content) {
		return StringUtils.startsWith(content, IdentificadorRegistroEnum.VENDA.getCodigo());
	}

}
