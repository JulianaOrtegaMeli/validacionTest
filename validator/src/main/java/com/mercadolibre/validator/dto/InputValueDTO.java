package com.mercadolibre.fashion.validator.plugin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InputValueDTO {

  private String id;

  private String name;

  public InputValueDTO(String id) {
    this.id = id;
  }
}
