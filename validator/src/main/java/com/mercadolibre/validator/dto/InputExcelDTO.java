package com.mercadolibre.fashion.validator.plugin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InputExcelDTO implements Comparable<InputExcelDTO> {

  private String gender;

  private String ageGroup;

  private String size;

  private String expectedFilterableSize;

  private Integer orden;

  @Override
  public int compareTo(@NotNull InputExcelDTO o) {
    return orden.compareTo(o.getOrden());
  }


}
