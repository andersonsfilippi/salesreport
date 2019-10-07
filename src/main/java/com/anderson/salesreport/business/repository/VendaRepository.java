package com.anderson.salesreport.business.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.anderson.salesreport.business.registro.importacao.Venda;

@Repository
public interface VendaRepository extends MongoRepository<Venda, Integer>{
	
	public Optional<Venda> findTopByOrderBySaleAmountDesc();

}
