package com.anderson.salesreport.business.registro.importacao;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Component
public class Item {
	private Integer idItem;
	private Integer itemQuantity;
	private Double itemPrice;
}
