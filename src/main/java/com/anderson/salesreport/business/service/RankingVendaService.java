package com.anderson.salesreport.business.service;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.limit;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Service;

import com.anderson.salesreport.business.registro.importacao.Venda;
import com.anderson.salesreport.business.repository.dto.RankingVendaDTO;

@Service
public class RankingVendaService {

	private MongoTemplate template;

	@Autowired
	public RankingVendaService(MongoTemplate template) {
		this.template = template;
	}

	public RankingVendaDTO getRankingPiorVendedor() {

		Aggregation agg = newAggregation(group("salesmanName").sum("saleAmount").as("totalVendas"),
		        project("totalVendas").and("salesmanName").previousOperation(), sort(Sort.Direction.ASC, "totalVendas"),
		        limit(1));

		AggregationResults<RankingVendaDTO> groupResults = template.aggregate(agg, Venda.class, RankingVendaDTO.class);

		return groupResults.getMappedResults().get(0);

	}

}
