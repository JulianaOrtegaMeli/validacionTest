package com.mercadolibre.validator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.mercadolibre.validator.util.AutomateTestFilterableUtils;

@SpringBootApplication
public class ValidatorApplication implements CommandLineRunner {


	//@Value("${automate-filterable-size.site}")
	String site = "MLA";


	//@Value("${automate-filterable-size.domain}")
	String domain = "T_SHIRTS";


	//@Value("${automate-filterable-size.file-path}")
	String file_path = "/Users/julortega/Documents/Casos de prueba/CASOS PRUEBAS REGEX TOPS.xlsx";

	//@Value("${automate-filterable-size.output_path}")
	String output_path = "/Users/julortega/Documents/Casos de prueba/";

	public static void main(String[] args) {
		SpringApplication.run(ValidatorApplication.class, args);
	}

	public void run(String... args) throws Exception {
		AutomateTestFilterableUtils automate = new AutomateTestFilterableUtils();
		automate.automateTest(domain, site, file_path, output_path);
	}


}
