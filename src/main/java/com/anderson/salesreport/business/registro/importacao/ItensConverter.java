package com.anderson.salesreport.business.registro.importacao;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.camel.dataformat.bindy.Format;
import org.apache.commons.lang3.StringUtils;

public class ItensConverter implements Format<List<Item>> {

	@Override
	public String format(List<Item> itens) throws Exception {
		return itens.toString();
	}

	@Override
	public List<Item> parse(String strItens) throws Exception {
		return parseItens(strItens);
	}

	private List<Item> parseItens(String strItens) {
		List<Item> itens = Pattern.compile(",").splitAsStream(StringUtils.replaceChars(strItens, "[]", ""))
		        .map(texto -> {
			        String[] dados = texto.split("-");
			        return new Item(Integer.valueOf(dados[0]), Integer.valueOf(dados[1]), Double.valueOf(dados[2]));
		        }).collect(Collectors.toList());
		return itens;
	}

}
