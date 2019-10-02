package com.anderson.salesreport.business.utils;

import static com.anderson.salesreport.business.registro.importacao.ImportacaoConstants.IDENTIFICADOR_REGISTRO_CLIENTE;
import static com.anderson.salesreport.business.registro.importacao.ImportacaoConstants.IDENTIFICADOR_REGISTRO_VENDA;
import static com.anderson.salesreport.business.registro.importacao.ImportacaoConstants.IDENTIFICADOR_REGISTRO_VENDEDOR;

public final class RegistroImportacaoUtils {
	private RegistroImportacaoUtils() {
	}

	public static boolean isContentVendedor(String content) {
		return isContentTipoRegistro(content, IDENTIFICADOR_REGISTRO_VENDEDOR);
	}

	public static boolean isContentCliente(String content) {
		return isContentTipoRegistro(content, IDENTIFICADOR_REGISTRO_CLIENTE);
	}

	public static boolean isContentVenda(String content) {
		return isContentTipoRegistro(content, IDENTIFICADOR_REGISTRO_VENDA);
	}

	private static boolean isContentTipoRegistro(String content, String tipoRegistro) {
		if (content == null) {
			return false;
		}
		return content.startsWith(tipoRegistro);
	}

}
