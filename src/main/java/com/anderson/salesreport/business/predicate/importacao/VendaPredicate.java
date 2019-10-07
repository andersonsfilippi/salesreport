package com.anderson.salesreport.business.predicate.importacao;

import org.apache.camel.Exchange;
import org.apache.camel.Predicate;
import org.springframework.stereotype.Component;

import com.anderson.salesreport.business.utils.RegistroImportacaoUtils;

@Component
public class VendaPredicate implements Predicate {

	@Override
	public boolean matches(Exchange exchange) {
		return RegistroImportacaoUtils.isContentVenda(exchange.getIn().getBody(String.class));
	}

}
