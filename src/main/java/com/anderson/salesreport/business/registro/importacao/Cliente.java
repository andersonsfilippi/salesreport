package com.anderson.salesreport.business.registro.importacao;

import static com.anderson.salesreport.business.registro.importacao.ImportacaoConstants.IDENTIFICADOR_REGISTRO_CLIENTE;

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
@Getter
@Component
@CsvRecord(separator = "ç")
public class Cliente implements RegistroImportacao{

	@DataField(pos = 1, required = true, trim = true)
	private final String identificadorRegistro = IDENTIFICADOR_REGISTRO_CLIENTE;
	
	@DataField(pos = 2, required = true, trim = true)
    private String cnpj;
	
	@DataField(pos = 3, required = true, trim = true)
    private String name;
	
	@DataField(pos = 4, required = true, trim = true)
    private String businessArea;

}
