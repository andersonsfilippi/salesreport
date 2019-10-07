package com.anderson.salesreport.business.route.importacao;

import static com.anderson.salesreport.business.configuration.Constants.WRAP_LINE_REGEX;
import static com.anderson.salesreport.business.configuration.Routes.DIRECT_BIND_CLIENTE;
import static com.anderson.salesreport.business.configuration.Routes.DIRECT_BIND_CLIENTE_ID;
import static com.anderson.salesreport.business.configuration.Routes.DIRECT_BIND_VENDA;
import static com.anderson.salesreport.business.configuration.Routes.DIRECT_BIND_VENDA_ID;
import static com.anderson.salesreport.business.configuration.Routes.DIRECT_BIND_VENDEDOR;
import static com.anderson.salesreport.business.configuration.Routes.DIRECT_BIND_VENDEDOR_ID;
import static com.anderson.salesreport.business.configuration.Routes.DIRECT_CRIA_RESUMO_IMPORTACAO;
import static com.anderson.salesreport.business.configuration.Routes.DIRECT_CRIA_RESUMO_IMPORTACAO_ID;
import static com.anderson.salesreport.business.configuration.Routes.DIRECT_MARSHAL_RESUMO_IMPORTACAO;
import static com.anderson.salesreport.business.configuration.Routes.DIRECT_MARSHAL_RESUMO_IMPORTACAO_ID;
import static com.anderson.salesreport.business.configuration.Routes.DIRECT_PARSE_BIND_CONTEUDO_ARQUIVO;
import static com.anderson.salesreport.business.configuration.Routes.DIRECT_PARSE_BIND_CONTEUDO_ARQUIVO_ID;
import static com.anderson.salesreport.business.configuration.Routes.DIRECT_PARSE_CONTEUDO_ARQUIVO;
import static com.anderson.salesreport.business.configuration.Routes.DIRECT_PARSE_CONTEUDO_ARQUIVO_ID;
import static com.anderson.salesreport.business.configuration.Routes.DIRECT_PROCESSAR_CRITICAS;
import static com.anderson.salesreport.business.configuration.Routes.DIRECT_PROCESSAR_CRITICAS_ID;
import static com.anderson.salesreport.business.configuration.Routes.DIRECT_SALVAR_ARQUIVO_RESUMO_IMPORTACAO;
import static com.anderson.salesreport.business.configuration.Routes.DIRECT_SALVAR_ARQUIVO_RESUMO_IMPORTACAO_ID;
import static org.apache.camel.builder.PredicateBuilder.not;

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
import com.anderson.salesreport.business.configuration.Constants;
import com.anderson.salesreport.business.predicate.importacao.ClientePredicate;
import com.anderson.salesreport.business.predicate.importacao.ExisteCriticasPredicate;
import com.anderson.salesreport.business.predicate.importacao.VendaPredicate;
import com.anderson.salesreport.business.predicate.importacao.VendedorPredicate;
import com.anderson.salesreport.business.processor.ProcessaCritica;
import com.anderson.salesreport.business.processor.ResumoCriticas;
import com.anderson.salesreport.business.registro.importacao.Cliente;
import com.anderson.salesreport.business.registro.importacao.RegistroImportacao;
import com.anderson.salesreport.business.registro.importacao.Venda;
import com.anderson.salesreport.business.registro.importacao.Vendedor;

@Component
public class ImportacaoRoute extends RouteBuilder{
	
	private VendedorPredicate isVendedor;
	private VendaPredicate isVenda;
	private ClientePredicate isCliente;
	private ExisteCriticasPredicate existemCriticas;
	private ResumoImportacaoBean resumoImportacao;
	private ProcessaCritica processaCritica;
	private ResumoCriticas resumoCriticas;
	private static final Logger LOGGER = LoggerFactory.getLogger(ImportacaoRoute.class);
	private String file_name_camel = "${headers." + Exchange.FILE_NAME + "}";

	
	@Autowired
	public ImportacaoRoute(VendedorPredicate isVendedor, VendaPredicate isVenda, ClientePredicate isCliente, 
			ExisteCriticasPredicate existemCriticas, ResumoImportacaoBean resumoImportacao, ProcessaCritica processaCritica,
			ResumoCriticas resumoCriticas) {
		super();
		this.isVendedor = isVendedor;
		this.isVenda = isVenda;
		this.isCliente = isCliente;
		this.existemCriticas = existemCriticas;
		this.resumoImportacao = resumoImportacao;
		this.processaCritica = processaCritica;
		this.resumoCriticas = resumoCriticas;
	}

	@Override
	public void configure() throws Exception {
		
		from("{{route.from}}")
		.log(LoggingLevel.INFO, LOGGER, buildStrLog("Convertendo dados do arquivo", "${headers." + Exchange.FILE_NAME + "}"))
		.to(DIRECT_PARSE_BIND_CONTEUDO_ARQUIVO);
		
		from(DIRECT_PARSE_BIND_CONTEUDO_ARQUIVO).routeId(DIRECT_PARSE_BIND_CONTEUDO_ARQUIVO_ID)
		.split(body().regexTokenize(WRAP_LINE_REGEX), new GroupedBodyAggregationStrategy<RegistroImportacao>(RegistroImportacao.class))
			.to(DIRECT_PARSE_CONTEUDO_ARQUIVO)
		.end()
		.choice()
	    	.when(not(existemCriticas))
		    	.log(LoggingLevel.INFO, LOGGER, buildStrLog("Arquivo", file_name_camel, "lido com sucesso!"))
		    	.pipeline(DIRECT_CRIA_RESUMO_IMPORTACAO, DIRECT_MARSHAL_RESUMO_IMPORTACAO)
		    	.log(LoggingLevel.INFO, LOGGER, buildStrLog("Relatorio", file_name_camel, "gerado com sucesso",body().toString()))
		    	.to(DIRECT_SALVAR_ARQUIVO_RESUMO_IMPORTACAO)
	    	.otherwise()
		    	.log(LoggingLevel.INFO, LOGGER, buildStrLog("Processamento do arquivo", "${headers." + Exchange.FILE_NAME + "}", "cancelado!"))
				.to(DIRECT_PROCESSAR_CRITICAS)
		.end();
		
		from(DIRECT_PARSE_CONTEUDO_ARQUIVO).routeId(DIRECT_PARSE_CONTEUDO_ARQUIVO_ID)
        .choice()
	    	.when(isVendedor)
	    		.log(LoggingLevel.INFO, LOGGER, buildStrLog("Vendedor encontrado no arquivo", "${headers." + Exchange.FILE_NAME + "}"))
	    		.to(DIRECT_BIND_VENDEDOR)
	        .when(isCliente)
	        	.log(LoggingLevel.INFO, LOGGER, buildStrLog("Cliente encontrado no arquivo", "${headers." + Exchange.FILE_NAME + "}"))
	        	.to(DIRECT_BIND_CLIENTE)
	        .when(isVenda)
		        .log(LoggingLevel.INFO, LOGGER, buildStrLog("Venda encontrada no arquivo ${headers." + Exchange.FILE_NAME + "}"))
	    		.to(DIRECT_BIND_VENDA)
		.end();
		
		from(DIRECT_CRIA_RESUMO_IMPORTACAO).routeId(DIRECT_CRIA_RESUMO_IMPORTACAO_ID)
		.log(LoggingLevel.INFO, LOGGER, buildStrLog("Criando resumo da importação do arquivo", "${headers." + Exchange.FILE_NAME + "}"))
		.bean(resumoImportacao);
		
		from(DIRECT_MARSHAL_RESUMO_IMPORTACAO).routeId(DIRECT_MARSHAL_RESUMO_IMPORTACAO_ID)
		.marshal().json(JsonLibrary.Jackson)
		.log(LoggingLevel.INFO, LOGGER, buildStrLog("Convertendo resumo para JSON para o arquivo", "${headers." + Exchange.FILE_NAME + "}"));
		
		from(DIRECT_SALVAR_ARQUIVO_RESUMO_IMPORTACAO).routeId(DIRECT_SALVAR_ARQUIVO_RESUMO_IMPORTACAO_ID)
		.to("{{route.to}}");
		
		from(DIRECT_PROCESSAR_CRITICAS).routeId(DIRECT_PROCESSAR_CRITICAS_ID)
		.log(LoggingLevel.INFO, LOGGER, buildStrLog("Críticas no arquivo", "${headers." + Exchange.FILE_NAME + "}:"))
        .process(resumoCriticas)
        .throwException(new CamelException("Processamento cancelado!"));
		
		configureBindy(DIRECT_BIND_VENDEDOR, DIRECT_BIND_VENDEDOR_ID, Vendedor.class);

		configureBindy(DIRECT_BIND_CLIENTE,	DIRECT_BIND_CLIENTE_ID,	Cliente.class);
		
		configureBindy(DIRECT_BIND_VENDA, DIRECT_BIND_VENDA_ID,	Venda.class);
		
	}
	
	private String buildStrLog(Object... mensagens) {
		return StringUtils.joinWith(" ", mensagens);
	}
	
	private void configureBindy(String routeEndpoint, String routeId, @SuppressWarnings("rawtypes") Class clazz) {
		
		String msg_erro = "Erro!";
		String[] msg_layout = { "Layout ou dados do registro", "inválidos! Processamento cancelado!" };
		String msg_processoSuceso = buildStrLog("Dados do registro", clazz.getSimpleName(), "importados com sucesso");
		
		from(routeEndpoint).routeId(routeId)
	 	.doTry()
	 	.unmarshal().bindy(BindyType.Csv, clazz)
	 		.log(LoggingLevel.INFO, LOGGER, msg_processoSuceso)
	 	.endDoTry()
	 	.doCatch(Exception.class)
			.setHeader(Constants.CRITICA_HEADER, simple(buildStrLog(msg_erro, msg_layout[0], clazz.getSimpleName(), msg_layout[1])))
	        .log(LoggingLevel.ERROR, LOGGER, buildStrLog("Erro:", "${headers." + Constants.CRITICA_HEADER + "} ${exception}"))
			.process(processaCritica)
	 	.end();
    }

}
