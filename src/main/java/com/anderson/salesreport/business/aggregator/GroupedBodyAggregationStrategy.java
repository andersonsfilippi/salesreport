package com.anderson.salesreport.business.aggregator;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AbstractListAggregationStrategy;

public class GroupedBodyAggregationStrategy<T>  extends AbstractListAggregationStrategy<T> {
	
	private Class<T> clazz;
	
	public GroupedBodyAggregationStrategy(Class<T> clazz) {
		super();
		this.clazz = clazz;
	}

	@Override
	public T getValue(Exchange exchange) {		
		T body = (T) exchange.getIn().getBody(clazz);		
		return body;
	}
	
	@Override
    public Exchange aggregate(Exchange exchangeAcumulator, Exchange newExchange) {
		Exchange aggregated = super.aggregate(exchangeAcumulator, newExchange);
		return aggregated;
	}

	
}
