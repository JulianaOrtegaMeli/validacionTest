package com.mercadolibre.validator.dto;

import lombok.*;

import java.util.*;

@Data
@AllArgsConstructor
public class OutputJsonDTO {

  public boolean success;

  public List<String> callbacks;

  public List<CauseDTO> causes;

  public OutputJsonDTO() {
    this.callbacks = new ArrayList<String>();
    this.causes = new ArrayList<CauseDTO>();
  }
}
