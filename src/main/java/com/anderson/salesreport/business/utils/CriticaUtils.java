package com.anderson.salesreport.business.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.camel.Exchange;

import com.anderson.salesreport.business.configuration.Constants;

public final class CriticaUtils {

	private CriticaUtils() {
	}

	@SuppressWarnings("unchecked")
	public static List<String> extrairCriticasExchange(Exchange exchange) {
		return Optional.ofNullable(exchange.getProperty(Constants.ERROS_PROPERTY, List.class))
				.orElse(new ArrayList<String>());
	}

	public static void adicionarCriticaExchange(final Exchange exchange, String critica) {
		List<String> criticaList = extrairCriticasExchange(exchange);
		if (critica != null && !criticaList.contains(critica)) {
			criticaList.add(critica);
			exchange.setProperty(Constants.ERROS_PROPERTY, criticaList);
		}
	}
	
}
