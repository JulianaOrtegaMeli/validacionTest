package com.mercadolibre.validator.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.validator.dto.*;
import com.mercadolibre.validator.dto.api.RequestColaider;
import java.io.IOException;
import java.util.*;

import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
public class TestValidation {

  public static String invokeApiCall(String myJsonString) {
    RestTemplate restTemplate = new RestTemplate();
    String result = "";
    ObjectMapper om = new ObjectMapper();

    /*String myJsonString =
        "{\n"
            + "    \"id\": \"MLA933769272\",\n"
            + "    \"site_id\": \"MLA\",\n"
            + "    \"category_id\": \"MLA109042\",\n"
            + "    \"attributes\": [\n"
            + "        {\n"
            + "            \"id\": \"AGE_GROUP\",\n"
            + "            \"name\": \"Edad\",\n"
            + "            \"value_id\": \"6725189\",\n"
            + "            \"value_name\": \"ADULTOS\",\n"
            + "            \"values\": [\n"
            + "                {\n"
            + "                    \"id\": \"6725189\",\n"
            + "                    \"name\": \"ADULTOS\"\n"
            + "                }\n"
            + "            ]\n"
            + "        },\n"
            + "        {\n"
            + "            \"id\": \"GENDER\",\n"
            + "            \"name\": \"Género\",\n"
            + "            \"value_id\": \"339665\",\n"
            + "            \"value_name\": \"Mujer\",\n"
            + "            \"values\": [\n"
            + "                {\n"
            + "                    \"id\": \"339665\",\n"
            + "                    \"name\": \"Mujer\"\n"
            + "                }\n"
            + "            ]\n"
            + "        }\n"
            + "    ],\n"
            + "    \"variations\": [\n"
            + "        {\n"
            + "            \"id\": \"93955252330\",\n"
            + "            \"attribute_combinations\": [\n"
            + "                {\n"
            + "                    \"id\": \"SIZE\",\n"
            + "                    \"name\": \"Talle\",\n"
            + "                    \"value_id\": \"11375715\",\n"
            + "                    \"value_name\": \"10 XL\",\n"
            + "                    \"values\": [\n"
            + "                        {\n"
            + "                            \"id\": \"11375715\",\n"
            + "                            \"name\": \"10 XL\"\n"
            + "                        }\n"
            + "                    ]\n"
            + "                }\n"
            + "            ],\n"
            + "            \"attributes\": []\n"
            + "        }\n"
            + "    ]\n"
            + "}";
        */
    try {
      RequestColaider root = om.readValue(myJsonString.getBytes(), RequestColaider.class);


        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("X-Client-Id", "2579503448603610");
        headers.add("X-Auth-Token", "ad6824d50e1a23d5f12debc5829759a9bed608726f52784e6bdd8847beca14c9");

        HttpEntity<Object> requestEntity = new HttpEntity<>(root, headers);
         String url="https://internal-api.mercadolibre.com/validation-hub/items/normalize?department=structured-data&validation=fashion-size-consistency&env=core-test&forwarded_client_id=2860837171021627";
         Object response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Object.class);
      ResponseEntity r = (ResponseEntity) response;
      Map<String, ArrayList <LinkedHashMap<String, String>>> map = (Map) r.getBody();
     ArrayList <LinkedHashMap<String, String>> array = map.get("causes");
     LinkedHashMap<String, String> link = array.get(0);
     String mess = link.get("message");
      String[] parts = mess.split(",");
      String[] p = parts[1].split("\\)");
       result = p[0];
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return result;
  }
}
