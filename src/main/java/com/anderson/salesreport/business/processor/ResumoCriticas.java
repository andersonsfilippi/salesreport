package com.anderson.salesreport.business.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import com.anderson.salesreport.business.utils.CriticaUtils;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ResumoCriticas implements Processor {

	@Override
	public void process(Exchange exchange) {
		log.error(String.join(System.lineSeparator(), CriticaUtils.extrairCriticasExchange(exchange)));
	}

}
