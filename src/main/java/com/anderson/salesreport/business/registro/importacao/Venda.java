package com.anderson.salesreport.business.registro.importacao;

import static com.anderson.salesreport.business.registro.importacao.ImportacaoConstants.IDENTIFICADOR_REGISTRO_VENDA;

import java.util.List;

import org.apache.camel.dataformat.bindy.annotation.BindyConverter;
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
@Component
@CsvRecord(separator = "ç")
public class Venda implements RegistroImportacao{
	
	@DataField(pos = 1, required = true, trim = true)
	private final String identificadorRegistro = IDENTIFICADOR_REGISTRO_VENDA;
	
	@DataField(pos = 2, required = true, trim = true)
    private String saleId;
	
	@DataField(pos = 3, required = true, trim = true)
	@BindyConverter(ItensConverter.class)
    private List<Item> itens;
	
	@DataField(pos = 4, required = true, trim = true)
    private Double salesmanName;

}
