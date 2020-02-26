package com.cdanner.b2wsw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		RestTemplate restTemplate = builder.build();
		restTemplate.getInterceptors().add(addRequestHeader());
		return restTemplate;
	}

	private ClientHttpRequestInterceptor addRequestHeader() {
		return (request, body, execution) -> {
			request.getHeaders().set("User-agent", "curl/7.60.0");
			return execution.execute(request, body);
		};
	}
}