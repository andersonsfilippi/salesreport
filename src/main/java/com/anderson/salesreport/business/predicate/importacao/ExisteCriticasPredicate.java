package com.anderson.salesreport.business.predicate.importacao;

import org.apache.camel.Exchange;
import org.apache.camel.Predicate;
import org.springframework.stereotype.Component;

import com.anderson.salesreport.business.utils.CriticaUtils;

@Component
public class ExisteCriticasPredicate implements Predicate {

	@Override
	public boolean matches(Exchange exchange) {
		return !CriticaUtils.extrairCriticasExchange(exchange).isEmpty();
	}


}
