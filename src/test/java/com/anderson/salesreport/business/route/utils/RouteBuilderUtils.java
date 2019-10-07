package com.anderson.salesreport.business.route.utils;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.AdviceWithRouteBuilder;


public class RouteBuilderUtils {

	
	public static void createInterceptedRoute(CamelContext camelContext, String originalRouteId, String destinEndpoint) throws Exception {
        camelContext.getRouteDefinitions()
        .stream()
        .filter(routeDefinition -> originalRouteId.equals(routeDefinition.getId()))
        .findFirst()
		.orElseThrow(() -> new IllegalArgumentException("Route with id " + originalRouteId + " not found."))
        .adviceWith(camelContext, new AdviceWithRouteBuilder() {
                @Override
                public void configure() throws Exception {
                	intercept()
                    .log("Intercepted route: "+destinEndpoint+ "\nBody: ${body}")
                    .to(destinEndpoint);
                }
        });

    }
}