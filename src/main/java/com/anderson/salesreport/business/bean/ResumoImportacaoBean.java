package com.anderson.salesreport.business.bean;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.apache.camel.Body;
import org.springframework.stereotype.Component;

import com.anderson.salesreport.business.registro.importacao.Cliente;
import com.anderson.salesreport.business.registro.importacao.RegistroImportacao;
import com.anderson.salesreport.business.registro.importacao.Venda;
import com.anderson.salesreport.business.registro.importacao.Vendedor;
import com.anderson.salesreport.business.service.bo.ResumoImportacao;

@Component
public class ResumoImportacaoBean {

	public ResumoImportacao transform(@Body List<RegistroImportacao> registros) {

		List<Cliente> clientes = extractRegistros(registros, Cliente.class);
		List<Venda> vendas = extractRegistros(registros, Venda.class);
		List<Vendedor> vendedores = extractRegistros(registros, Vendedor.class);

		Venda maiorVenda = obterMaiorVenda(vendas);
		Venda menorVenda = obterMenorVenda(vendas);

		return ResumoImportacao.builder()
				.qtdClientes(clientes.size())
				.qtdVendedores(vendedores.size())
		        .idMaiorVenda(maiorVenda.getSaleId())
		        .piorVendedor(obterPiorVendedor(vendedores, menorVenda))
		        .build();
	}

	private Vendedor obterPiorVendedor(final List<Vendedor> vendedores, Venda venda) {
		return vendedores.stream().filter(v -> v.getName().equals(venda.getSalesmanName())).findFirst().orElseGet(null);
	}

	private Venda obterMaiorVenda(final List<Venda> vendas) {
		return calcularVenda(vendas, true);
	}

	private Venda obterMenorVenda(final List<Venda> vendas) {
		return calcularVenda(vendas, false);
	}

	private Venda calcularVenda(final List<Venda> vendas, boolean isTopRank) {
		if (isTopRank)
			return vendas.stream().max(Comparator.comparing(Venda::totalVendas))
			        .orElseThrow(NoSuchElementException::new);
		else
			return vendas.stream().min(Comparator.comparing(Venda::totalVendas))
			        .orElseThrow(NoSuchElementException::new);
	}

	@SuppressWarnings("unchecked")
	private <T extends RegistroImportacao> List<T> extractRegistros(List<? extends RegistroImportacao> registros,
	        Class<T> clazz) {
		return registros.stream().filter(registro -> clazz.equals(registro.getClass())).map(item -> (T) item)
		        .collect(Collectors.toList());
	}

}
