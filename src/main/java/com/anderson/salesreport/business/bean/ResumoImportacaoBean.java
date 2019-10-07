package com.anderson.salesreport.business.bean;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.camel.Body;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anderson.salesreport.business.registro.importacao.Cliente;
import com.anderson.salesreport.business.registro.importacao.RegistroImportacao;
import com.anderson.salesreport.business.registro.importacao.Venda;
import com.anderson.salesreport.business.registro.importacao.Vendedor;
import com.anderson.salesreport.business.repository.dto.RankingVendaDTO;
import com.anderson.salesreport.business.service.RankingVendaService;
import com.anderson.salesreport.business.service.VendaService;
import com.anderson.salesreport.business.service.VendedorService;
import com.anderson.salesreport.business.service.bo.ResumoImportacao;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ResumoImportacaoBean {

	private VendedorService vendedorService;
	private VendaService vendaService;
	private RankingVendaService rankingVendaService;
	
	
	@Autowired
	public ResumoImportacaoBean(VendedorService vendedorService, VendaService vendaService, RankingVendaService rankingVendaService) {
		this.vendedorService = vendedorService;
		this.vendaService = vendaService;
		this.rankingVendaService = rankingVendaService;
	}

	public ResumoImportacao transform(@Body List<RegistroImportacao> registros) {
				
		List<Cliente> clientes = extractRegistros(registros, Cliente.class);
		List<Venda> vendas = extractRegistros(registros, Venda.class);
		List<Vendedor> vendedores = extractRegistros(registros, Vendedor.class);

		vendedorService.salvar(vendedores);
		vendaService.processarVendas(vendas);
		
		Optional<Venda> maiorVenda = vendaService.obterMaiorVenda();
		RankingVendaDTO piorVendedorRanking = rankingVendaService.getRankingPiorVendedor();

		if(!maiorVenda.isPresent()) {
			log.warn("Nenhuma venda válida cadastrada no sistema!");
		}

		return ResumoImportacao.builder()
		        .qtdClientes(ObjectUtils.defaultIfNull(clientes.size(), 0))
		        .qtdVendedores(ObjectUtils.defaultIfNull(vendedores.size(), 0))
		        .idMaiorVenda(ObjectUtils.defaultIfNull(maiorVenda.get().getSaleId(), 0))
		        .piorVendedorRanking(ObjectUtils.defaultIfNull(piorVendedorRanking,RankingVendaDTO.builder().build()))
		        .build();
	}


	@SuppressWarnings("unchecked")
	private <T extends RegistroImportacao> List<T> extractRegistros(List<? extends RegistroImportacao> registros, Class<T> clazz) {
			return registros.stream().filter(
					registro -> clazz.equals(registro.getClass())).map(item -> ((T) item))
			        .collect(Collectors.toList());			
	}

}
