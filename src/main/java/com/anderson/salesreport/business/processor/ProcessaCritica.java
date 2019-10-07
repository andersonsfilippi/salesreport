package com.anderson.salesreport.business.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import com.anderson.salesreport.business.configuration.Constants;
import com.anderson.salesreport.business.utils.CriticaUtils;

@Component
public class ProcessaCritica implements Processor {

	@Override
    public void process(final Exchange exchange) throws Exception {
        CriticaUtils.adicionarCriticaExchange(exchange, extrairCritica(exchange));
    }

	public String extrairCritica(final Exchange exchange) {
		return (String) exchange.getIn().getHeader(Constants.CRITICA_HEADER);
	}

}
