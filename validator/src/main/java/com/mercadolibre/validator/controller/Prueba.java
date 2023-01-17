package com.mercadolibre.validator.controller;

import com.mercadolibre.validator.service.TestValidation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class Prueba {


    private TestValidation validationService;

    public Prueba(TestValidation validationService) {
        this.validationService = validationService;
    }

    @GetMapping("/pruebaValidador/test")
    public void test(){
        //validationService.invokeApiCall();
    }
}
