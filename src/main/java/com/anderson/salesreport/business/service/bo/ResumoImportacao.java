package com.anderson.salesreport.business.service.bo;

import org.springframework.stereotype.Component;

import com.anderson.salesreport.business.repository.dto.RankingVendaDTO;

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
	private Integer idMaiorVenda;
	private RankingVendaDTO piorVendedorRanking;
	
}
