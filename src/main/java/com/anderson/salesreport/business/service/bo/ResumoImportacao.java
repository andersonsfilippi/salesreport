package com.anderson.salesreport.business.service.bo;

import org.springframework.stereotype.Component;

import com.anderson.salesreport.business.registro.importacao.Vendedor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
@EqualsAndHashCode
public class ResumoImportacao {
	private Integer qtdClientes;
	private Integer qtdVendedores;
	private String idMaiorVenda;
	private Vendedor piorVendedor;
	
}
