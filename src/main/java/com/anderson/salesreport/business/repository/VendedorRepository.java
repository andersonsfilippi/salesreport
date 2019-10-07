package com.anderson.salesreport.business.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.anderson.salesreport.business.registro.importacao.Vendedor;

@Repository
public interface VendedorRepository extends MongoRepository<Vendedor, String>{
	
	public Vendedor findByCpf(String name);
	public Vendedor findByName(String name);
}
