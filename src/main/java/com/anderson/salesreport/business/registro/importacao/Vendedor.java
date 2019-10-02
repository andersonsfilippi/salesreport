package com.anderson.salesreport.business.registro.importacao;

import static com.anderson.salesreport.business.registro.importacao.ImportacaoConstants.IDENTIFICADOR_REGISTRO_VENDEDOR;

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
public class Vendedor implements RegistroImportacao{

	@DataField(pos = 1, required = true, trim = true)
	private final String identificadorRegistro = IDENTIFICADOR_REGISTRO_VENDEDOR;
	
	@DataField(pos = 2, required = true, trim = true)
    private String cpf;
	
	@DataField(pos = 3, required = true, trim = true)
    private String name;
	
	@DataField(pos = 4, required = true, trim = true)
    private Double salary;

}
