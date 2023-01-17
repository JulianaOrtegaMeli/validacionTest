package com.mercadolibre.validator.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class Prueba {

    @GetMapping("/pruebaValidador/test")
    public void test(){

    }
}
