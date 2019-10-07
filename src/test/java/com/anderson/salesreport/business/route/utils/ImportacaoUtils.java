package com.anderson.salesreport.business.route.utils;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.anderson.salesreport.business.registro.importacao.Cliente;
import com.anderson.salesreport.business.registro.importacao.Item;
import com.anderson.salesreport.business.registro.importacao.Venda;
import com.anderson.salesreport.business.registro.importacao.Vendedor;
import com.anderson.salesreport.business.repository.dto.RankingVendaDTO;
import com.anderson.salesreport.business.service.bo.ResumoImportacao;

public class ImportacaoUtils {


	private static final String CARACTERE_SEPARADOR = "ç";
	
	// Registro 001 - Vendedor
	private static final String TIPO_REGISTRO_VENDEDOR = "001";
	private static final String CPF_VENDEDOR = "1234567891234";
	private static final String NOME_VENDEDOR = "Pedro";
	private static final String SALARIO_VENDEDOR = "50000";

	// Registro 002 - Cliente
	private static final String TIPO_REGISTRO_CLIENTE = "002";
	private static final String CNPJ_CLIENTE = "2345675434544345";
	private static final String NOME_CLIENTE = "Jose da Silva";
	private static final String RAMO_CLIENTE = "Rural";

	// Registro 003 - Venda
	private static final String TIPO_REGISTRO_VENDA = "003";
	private static final String ID_VENDA = "10";
	private static final String ITENS_VENDA = "[1-10-100,2-30-2.50,3-40-3.10]";
	private static final String VENDEDOR_VENDA = "Pedro";
	private static final String TOTAL_VENDA = "1199";
	
	public static final ResumoImportacao resumo() {
		return ResumoImportacao.builder()
				.qtdClientes(1)
				.qtdVendedores(1)
				.idMaiorVenda(10)
				.piorVendedorRanking(RankingVendaDTO.builder()
						.salesmanName("Pedro")
						.totalVendas(Double.valueOf("1199")).build()
				).build();
	}


	public static Vendedor vendedor() {
		return Vendedor.builder()
				.cpf(CPF_VENDEDOR)
				.name(NOME_VENDEDOR)
				.salary(Double.valueOf(SALARIO_VENDEDOR))
		        .build();
	}

	public static Cliente cliente() {
		return Cliente.builder()
				.cnpj(CNPJ_CLIENTE)
				.name(NOME_CLIENTE)
				.businessArea(RAMO_CLIENTE)
		        .build();
	}

	public static Venda venda() {
		return Venda.builder()
				.saleId(Integer.valueOf(ID_VENDA))
				.itens(converterItens(ITENS_VENDA))
				.salesmanName(VENDEDOR_VENDA)
				.saleAmount(Double.valueOf(TOTAL_VENDA))
		        .build();
	}

	private static List<Item> converterItens(String itensVenda) {
		List<Item> itens = Pattern.compile(",").splitAsStream(StringUtils.replaceChars(itensVenda, "[]", ""))
		        .map(texto -> {
			        String[] dados = texto.split("-");
			        return new Item(Integer.valueOf(dados[0]), Integer.valueOf(dados[1]), Double.valueOf(dados[2]));
		        }).collect(Collectors.toList());
		return itens;
		
	}

	public static String montarLinhaVendedor() {
		StringBuilder str = new StringBuilder(TIPO_REGISTRO_VENDEDOR);
		str.append(CARACTERE_SEPARADOR);
		str.append(CPF_VENDEDOR);
		str.append(CARACTERE_SEPARADOR);
		str.append(NOME_VENDEDOR);
		str.append(CARACTERE_SEPARADOR);
		str.append(SALARIO_VENDEDOR);
		return str.toString();
	}

	public static String montarLinhaCliente() {
		StringBuilder str = new StringBuilder(TIPO_REGISTRO_CLIENTE);
		str.append(CARACTERE_SEPARADOR);
		str.append(CNPJ_CLIENTE);
		str.append(CARACTERE_SEPARADOR);
		str.append(NOME_CLIENTE);
		str.append(CARACTERE_SEPARADOR);
		str.append(RAMO_CLIENTE);
		return str.toString();
	}

	public static String montarLinhaVenda() {
		StringBuilder str = new StringBuilder(TIPO_REGISTRO_VENDA);
		str.append(CARACTERE_SEPARADOR);
		str.append(ID_VENDA);
		str.append(CARACTERE_SEPARADOR);
		str.append(ITENS_VENDA);
		str.append(CARACTERE_SEPARADOR);
		str.append(VENDEDOR_VENDA);
		return str.toString();
	}

	public static String montarBodyArquivo() {
		StringBuilder str = new StringBuilder();
		str.append(montarLinhaVendedor());
		str.append(StringUtils.LF);
		str.append(montarLinhaCliente());
		str.append(StringUtils.LF);
		str.append(montarLinhaVenda());
		return str.toString();
	}

}
