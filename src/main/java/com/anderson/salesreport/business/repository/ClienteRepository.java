package com.anderson.salesreport.business.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.anderson.salesreport.business.registro.importacao.Cliente;

@Repository
public interface ClienteRepository extends MongoRepository<Cliente, Integer>{
}
