package com.anderson.salesreport.business.route.importacao;

import static com.anderson.salesreport.business.configuration.Routes.DIRECT_BIND_CLIENTE;
import static com.anderson.salesreport.business.configuration.Routes.DIRECT_BIND_CLIENTE_ID;
import static com.anderson.salesreport.business.configuration.Routes.DIRECT_BIND_VENDA;
import static com.anderson.salesreport.business.configuration.Routes.DIRECT_BIND_VENDA_ID;
import static com.anderson.salesreport.business.configuration.Routes.DIRECT_BIND_VENDEDOR;
import static com.anderson.salesreport.business.configuration.Routes.DIRECT_BIND_VENDEDOR_ID;
import static com.anderson.salesreport.business.configuration.Routes.DIRECT_CRIA_RESUMO_IMPORTACAO;
import static com.anderson.salesreport.business.configuration.Routes.DIRECT_CRIA_RESUMO_IMPORTACAO_ID;
import static com.anderson.salesreport.business.configuration.Routes.DIRECT_PARSE_CONTEUDO_ARQUIVO;
import static com.anderson.salesreport.business.configuration.Routes.DIRECT_PARSE_CONTEUDO_ARQUIVO_ID;

import org.apache.camel.CamelException;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.BindyType;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anderson.salesreport.business.aggregator.GroupedBodyAggregationStrategy;
import com.anderson.salesreport.business.bean.ResumoImportacaoBean;
import com.anderson.salesreport.business.predicate.importacao.ClientePredicate;
import com.anderson.salesreport.business.predicate.importacao.VendaPredicate;
import com.anderson.salesreport.business.predicate.importacao.VendedorPredicate;
import com.anderson.salesreport.business.registro.importacao.Cliente;
import com.anderson.salesreport.business.registro.importacao.RegistroImportacao;
import com.anderson.salesreport.business.registro.importacao.Venda;
import com.anderson.salesreport.business.registro.importacao.Vendedor;
import com.anderson.salesreport.business.service.bo.ResumoImportacao;

@Component
public class ImportacaoRoute extends RouteBuilder{
	
	private VendedorPredicate isVendedor;
	private VendaPredicate isVenda;
	private ClientePredicate isCliente;
	private ResumoImportacaoBean resumoImportacao;
	private static final String WRAP_LINE_REGEX = "\\r\\n|\\n|\\r";
	private static final Logger LOGGER = LoggerFactory.getLogger(ImportacaoRoute.class);
	private String file_name_camel = "${headers." + Exchange.FILE_NAME + "}";

	
	@Autowired
	public ImportacaoRoute(VendedorPredicate isVendedor, VendaPredicate isVenda, ClientePredicate isCliente, ResumoImportacaoBean resumoImportacao) {
		super();
		this.isVendedor = isVendedor;
		this.isVenda = isVenda;
		this.isCliente = isCliente;
		this.resumoImportacao = resumoImportacao;
	}

	@Override
	public void configure() throws Exception {
		
		from("{{route.from}}")
			.log(LoggingLevel.INFO, LOGGER, "Inicio BIND. ${headers." + Exchange.FILE_NAME + "}")
			.split(body().regexTokenize(WRAP_LINE_REGEX), new GroupedBodyAggregationStrategy<RegistroImportacao>(RegistroImportacao.class))
				.to(DIRECT_PARSE_CONTEUDO_ARQUIVO)
			.end()
			.log(LoggingLevel.INFO, LOGGER, "------------------->>>>>> Arquivo lido: " + file_name_camel)
			
			.to(DIRECT_CRIA_RESUMO_IMPORTACAO)
			.marshal().json(JsonLibrary.Jackson)
			.log(LoggingLevel.INFO, LOGGER, "------------------->>>>>> Relatorio gerado: " + body().toString())
			.to("{{route.to}}");
		
		
		from(DIRECT_PARSE_CONTEUDO_ARQUIVO).routeId(DIRECT_PARSE_CONTEUDO_ARQUIVO_ID)
        .choice()
	    	.when(isVendedor)
	    		.log(LoggingLevel.INFO, LOGGER, "Vendedor encontrado no arquivo ${headers." + Exchange.FILE_NAME + "}")
	    		.to(DIRECT_BIND_VENDEDOR)
	        .when(isCliente)
	        	.log(LoggingLevel.INFO, LOGGER, "Cliente encontrado no arquivo ${headers." + Exchange.FILE_NAME + "}")
	        	.to(DIRECT_BIND_CLIENTE)
	        .when(isVenda)
		        .log(LoggingLevel.INFO, LOGGER, "Venda encontrada no arquivo ${headers." + Exchange.FILE_NAME + "}")
	    		.to(DIRECT_BIND_VENDA)
		.end();
		
		from(DIRECT_CRIA_RESUMO_IMPORTACAO).routeId(DIRECT_CRIA_RESUMO_IMPORTACAO_ID)
		.log(LoggingLevel.INFO, LOGGER, "Criando resumo da importação. ${headers." + Exchange.FILE_NAME + "}")
		.bean(resumoImportacao);
		
		configureBindy(DIRECT_BIND_VENDEDOR,
				DIRECT_BIND_VENDEDOR_ID,
				 Vendedor.class);

		configureBindy(DIRECT_BIND_CLIENTE,
				DIRECT_BIND_CLIENTE_ID,
				Cliente.class);
		
		configureBindy(DIRECT_BIND_VENDA,
				DIRECT_BIND_VENDA_ID,
				Venda.class);
		
	}
	
	private void configureBindy(String routeEndpoint, String routeId, @SuppressWarnings("rawtypes") Class clazz) {
		
		String msg_erro = "Erro!";
		String[] msg_layout = { "Layout ou dados do arquivo", "inválidos! Processamento cancelado!" };
		String msg_processoSuceso = StringUtils.joinWith(" ", "Dados do registro", clazz.getSimpleName(),
		        "importados com sucesso");
		
		from(routeEndpoint).routeId(routeId)
	 	.doTry()
	 	.unmarshal().bindy(BindyType.Csv, clazz)
	 		.log(LoggingLevel.INFO, LOGGER, msg_processoSuceso)
	 	.endDoTry()
	 	.doCatch(Exception.class)
		        .log(LoggingLevel.ERROR, LOGGER, StringUtils.joinWith(" ",msg_erro, msg_layout[0],"${headers." + Exchange.FILE_NAME + "}",msg_layout[1]))
		        .throwException(new CamelException(StringUtils.joinWith(" ",msg_erro, msg_layout[0],"${headers." + Exchange.FILE_NAME + "}",msg_layout[1])))
	 	.end();
    }

//		.process(new Processor() {
//			
//			@Override
//			public void process(Exchange exchange) throws Exception {
//				log.info("Exchange----- "+exchange.getIn().getBody(String.class).toString());
//				
//			}
//		})
}