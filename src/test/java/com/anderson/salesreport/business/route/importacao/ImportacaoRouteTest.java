package com.anderson.salesreport.business.route.importacao;

import static com.anderson.salesreport.business.configuration.Routes.DIRECT_PARSE_BIND_CONTEUDO_ARQUIVO;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import com.anderson.salesreport.business.bean.ResumoImportacaoBean;
import com.anderson.salesreport.business.configuration.Routes;
import com.anderson.salesreport.business.predicate.importacao.ClientePredicate;
import com.anderson.salesreport.business.predicate.importacao.ExisteCriticasPredicate;
import com.anderson.salesreport.business.predicate.importacao.VendaPredicate;
import com.anderson.salesreport.business.predicate.importacao.VendedorPredicate;
import com.anderson.salesreport.business.processor.ProcessaCritica;
import com.anderson.salesreport.business.processor.ResumoCriticas;
import com.anderson.salesreport.business.registro.importacao.RegistroImportacao;
import com.anderson.salesreport.business.route.TestConstants;
import com.anderson.salesreport.business.route.utils.ImportacaoUtils;
import com.anderson.salesreport.business.route.utils.RouteBuilderUtils;
import com.anderson.salesreport.business.service.RankingVendaService;
import com.anderson.salesreport.business.service.bo.ResumoImportacao;

@RunWith(CamelSpringBootRunner.class)
@SpringBootTest
public class ImportacaoRouteTest{

	@Spy
	private ExisteCriticasPredicate existemCriticas;

	@Spy
	private ClientePredicate isCliente;

	@Spy
	private VendaPredicate isVenda;

	@Spy
	private VendedorPredicate isVendedor;

	@Mock
	private ProcessaCritica processaCritica;

	@Mock
	private ResumoCriticas resumoCriticas;
	
	@Mock	
	private RankingVendaService rankingVendaService;

	@Spy
	@InjectMocks
	private ResumoImportacaoBean resumoImportacao;
	
	@InjectMocks
	private ImportacaoRoute importacaoRoute;
	
	@EndpointInject(uri = TestConstants.URI_MOCK_OUTPUT)
	private MockEndpoint mockEndpoint;
	
	@Autowired
	private ProducerTemplate template;

	@DirtiesContext
	@Test
	public void deveRealizarCorretamenteOBindyDeArquivoValido() throws Exception {
		RouteBuilderUtils.createInterceptedRoute(template.getCamelContext(), Routes.DIRECT_CRIA_RESUMO_IMPORTACAO_ID, TestConstants.URI_MOCK_OUTPUT);
		template.sendBody(Routes.DIRECT_PARSE_BIND_CONTEUDO_ARQUIVO, ImportacaoUtils.montarBodyArquivo());
		@SuppressWarnings("unchecked")
		List<RegistroImportacao> bodies = (List<RegistroImportacao>) mockEndpoint.getExchanges().get(0).getIn().getBody();
		assertFalse(bodies.isEmpty());
		assertEquals(bodies.size(), 3);
		assertThat(bodies,containsInAnyOrder(ImportacaoUtils.vendedor(),ImportacaoUtils.cliente(),ImportacaoUtils.venda()));
	}
	
	@DirtiesContext
	@Test
	public void deveCriarCorretamenteArquivoResultado() throws Exception {
		RouteBuilderUtils.createInterceptedRoute(template.getCamelContext(), Routes.DIRECT_MARSHAL_RESUMO_IMPORTACAO_ID, TestConstants.URI_MOCK_OUTPUT);
		template.sendBodyAndHeader(DIRECT_PARSE_BIND_CONTEUDO_ARQUIVO, ImportacaoUtils.montarBodyArquivo(),Exchange.FILE_NAME,"teste.txt");
		
		ResumoImportacao reumoImportacao = mockEndpoint.getExchanges().get(0).getIn().getBody(ResumoImportacao.class);
		assertEquals(reumoImportacao, ImportacaoUtils.resumo());
	}

}
