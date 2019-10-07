package com.anderson.salesreport.business.registro.importacao;

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
public class Cliente implements RegistroImportacao{

	@Transient
	@DataField(pos = 1, required = true, trim = true)
	private final String identificadorRegistro = IdentificadorRegistroEnum.CLIENTE.getCodigo();
	
	@Id
	@DataField(pos = 2, required = true, trim = true)
    private String cnpj;
	
	@DataField(pos = 3, required = true, trim = true)
    private String name;
	
	@DataField(pos = 4, required = true, trim = true)
    private String businessArea;

}
