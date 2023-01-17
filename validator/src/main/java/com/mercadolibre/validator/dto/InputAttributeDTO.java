package com.mercadolibre.validator.dto;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InputAttributeDTO {

  private String id;

  private String name;

  @SerializedName("value_id")
  private String valueId;

  @SerializedName("value_name")
  private String valueName;

  private List<InputValueDTO> values;

  public InputAttributeDTO(String id, String name, String valueId, String idValue) {
    this.id = id;
    this.name = name;
    this.valueId = valueId;

    this.values = new ArrayList<InputValueDTO>();
    InputValueDTO value = new InputValueDTO(idValue);
    this.values.add(value);
  }

  public InputAttributeDTO(String id, String name) {
    this.id = id;
    this.name = name;
    this.values = new ArrayList<InputValueDTO>();
    InputValueDTO value = new InputValueDTO();
    this.values.add(value);
  }
}
