package com.mercadolibre.validator.dto;

import lombok.*;

import java.util.*;

@Data
@AllArgsConstructor
public class OutputJsonDTO {

  public boolean success;

  public List<String> callbacks;

  public List <LinkedHashMap<String, String>> causes;

  public OutputJsonDTO() {
    this.callbacks = new ArrayList<String>();
    this.causes = new ArrayList <LinkedHashMap<String, String>>();
  }
}
