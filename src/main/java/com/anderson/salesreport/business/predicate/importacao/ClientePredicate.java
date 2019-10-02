package com.anderson.salesreport.business.predicate.importacao;

import org.apache.camel.Exchange;
import org.apache.camel.Predicate;
import org.springframework.stereotype.Component;

import com.anderson.salesreport.business.utils.RegistroImportacaoUtils;

@Component
public class ClientePredicate implements Predicate {

	@Override
	public boolean matches(Exchange exchange) {
		return RegistroImportacaoUtils.isContentCliente(exchange.getIn().getBody(String.class));
	}

}
