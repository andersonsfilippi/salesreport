package com.anderson.salesreport.business.registro.importacao;

import java.util.List;

import org.apache.camel.dataformat.bindy.annotation.BindyConverter;
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.stereotype.Component;

import com.anderson.salesreport.business.enums.IdentificadorRegistroEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
@Getter
@CsvRecord(separator = "ç")
public class Venda implements RegistroImportacao{
	
	@Transient
	@DataField(pos = 1, required = true, trim = true)
	private final String identificadorRegistro = IdentificadorRegistroEnum.VENDA.getCodigo();
	
	@Id
	@DataField(pos = 2, required = true, trim = true)
    private Integer saleId;
	
	@DataField(pos = 3, required = true, trim = true)
	@BindyConverter(ItensConverter.class)
    private List<Item> itens;
	
	@DataField(pos = 4, required = true, trim = true)
    private String salesmanName;
	
	private Double saleAmount;
	
	public Double getTotalVenda() {
		this.saleAmount = this.itens.stream().mapToDouble(x -> (x.getItemPrice() * x.getItemQuantity())).sum();
		return this.saleAmount;
	}

}
