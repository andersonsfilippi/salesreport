package com.anderson.salesreport.business.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.anderson.salesreport.business.enums.OcorrenciaVendaEnum;
import com.anderson.salesreport.business.registro.importacao.Venda;
import com.anderson.salesreport.business.repository.VendaRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class VendaService {

	private VendaRepository repository;
	private VendedorService vendedorService;

	@Autowired
	public VendaService(VendaRepository repository, VendedorService vendedorService) {
		this.repository = repository;
		this.vendedorService = vendedorService;
	}

	public void processarVendas(List<Venda> vendas) {

		validarVendedor(vendas);
		totalizarVendas(vendas);
		salvar(vendas);
	}
	
	private void totalizarVendas(List<Venda> vendas) {
		vendas.stream().iterator().forEachRemaining(v->{
			v.getTotalVenda();
		});
		
	}

	private void validarVendedor(List<Venda> vendas) {
		List<Venda>invalidas = vendas.stream()
				.filter(v2->(!isVendedorCadastrado(v2.getSalesmanName())))
				.collect(Collectors.toList());
		
		if (!CollectionUtils.isEmpty(invalidas)) {
			log.error(OcorrenciaVendaEnum.VENDEDOR_INVALIDO.getMensagem() + invalidas.toString());
		}
		
		vendas.removeAll(invalidas);
		
	}
	
	private void salvar(List<Venda> vendas) {
		repository.saveAll(vendas);
	}
	
	private boolean isVendedorCadastrado(String salesmanName) {
		return vendedorService.buscar(salesmanName).isPresent();
	}
	
	public Optional<Venda> obterMaiorVenda() {
		return repository.findTopByOrderBySaleAmountDesc();
	}
}
