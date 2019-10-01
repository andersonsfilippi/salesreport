package com.anderson.salesreport.business.registro.importacao;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@Builder
@AllArgsConstructor
@Component
public class Item {
	private int idItem;
	private int itemQuantity;
	private Double itemPrice;
	
}
