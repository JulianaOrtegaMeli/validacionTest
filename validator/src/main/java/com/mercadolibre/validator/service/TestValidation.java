package com.mercadolibre.validator.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.validator.dto.*;
import com.mercadolibre.validator.dto.api.*;

import java.io.*;
import java.util.*;

import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
public class TestValidation {

    public static RestTemplate restTemplate;

    public static ObjectMapper objectMapper;
    public static String url= "https://internal-api.mercadolibre.com/validation-hub/items/normalize?department=structured-data&validation=fashion-size-consistency&env=core-test&forwarded_client_id=2860837171021627";

    public static String buildRequest(String myJsonString) throws IOException {
        String result = "";
        restTemplate = new RestTemplate();
        objectMapper = new ObjectMapper();

        try {
            result = invokeApiCall(myJsonString);
        }catch (Exception e){
            result = e.getMessage().toString();
            if (!result.isEmpty()) {
                result = invokeApiCall(myJsonString);
            }
        }

        return result;
    }

    public static String invokeApiCall(String myJsonString) throws IOException {

      RequestColaider root = objectMapper.readValue(myJsonString.getBytes(), RequestColaider.class);
      HttpHeaders headers = new HttpHeaders();
      headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
      headers.setContentType(MediaType.APPLICATION_JSON);
      headers.add("X-Client-Id", "2579503448603610");
      headers.add("X-Auth-Token", "ad6824d50e1a23d5f12debc5829759a9bed608726f52784e6bdd8847beca14c9");

      HttpEntity<Object> requestEntity = new HttpEntity<>(root, headers);
        ResponseEntity r = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Object.class);
      return getFilterableSize(r);
    }

  private static String getFilterableSize (ResponseEntity response){
      String result = "";
      Map<String, ArrayList <LinkedHashMap<String, String>>> body = (Map) response.getBody();
      OutputJsonDTO output = new OutputJsonDTO();
      output.setCauses(body.get("causes"));
      if (!output.getCauses().isEmpty()) {
          String mess = output.getCauses().get(0).get("message");
          String[] parts = mess.split("\\(");
           result = getMultiValue(parts).toString();
      }
      return result;
  }

  private static StringBuilder getMultiValue (String [] parts) {
      String result = "";
      StringBuilder multivalue = new StringBuilder();
      String[] seccion, aux;
      for (int part = 1; part < parts.length; part++) {
          seccion = parts[part].split("\\)");
          aux = seccion[0].split(",");
          if (aux.length >= 2) {
              result = aux[1];
              if (aux.length > 2) {
                  result = aux[1] + "," + aux[2];
              }
          }
          if (part != parts.length - 1) {
              result += "|";
          }
          multivalue.append(result);
      }
      return multivalue;
  }
}
