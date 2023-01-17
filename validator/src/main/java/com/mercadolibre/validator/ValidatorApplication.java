package com.mercadolibre.validator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.mercadolibre.validator.util.AutomateTestFilterableUtils;

@SpringBootApplication
public class ValidatorApplication {


	@Value("${automate-filterable-size.site}")
	String site;


	@Value("${automate-filterable-size.domain}")
	String domain;


	@Value("${automate-filterable-size.file-path}")
	String file_path;

	@Value("${automate-filterable-size.output_path}")
	String output_path;

	public static void main(String[] args) {
		//ScopeUtils.calculateScopeSuffix();
		SpringApplication.run(ValidatorApplication.class, args);
	}

	public void run(String... args) throws Exception {

		//ColliderRestClient restClient = new ColliderRestClient(new ObjectMapper(), "http://localhost:8080/validation-hub/items/normalize", 3000, 3000, "");
		//RoutingHelper.createAndSetNewMeliContext();
		AutomateTestFilterableUtils automate = new AutomateTestFilterableUtils();
		automate.automateTest(domain, site, file_path, output_path);
	}


}
