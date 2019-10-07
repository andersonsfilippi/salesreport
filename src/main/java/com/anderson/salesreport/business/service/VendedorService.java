package com.anderson.salesreport.business.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anderson.salesreport.business.registro.importacao.Vendedor;
import com.anderson.salesreport.business.repository.VendedorRepository;

@Service
public class VendedorService {
	
	private VendedorRepository repository;
	
	@Autowired
	public VendedorService(VendedorRepository repository) {
		this.repository = repository;
	}
	
	public void salvar(Vendedor vendedor) {
		repository.save(vendedor);
	}
	
	public void salvar(List<Vendedor> vendedores) {
		repository.saveAll(vendedores);
	}
	
	public Optional<Vendedor>buscar(String nome) {
		return Optional.ofNullable(repository.findByName(nome));
	}
	
	public List<Vendedor> listar() {
		return repository.findAll();
	}

}
