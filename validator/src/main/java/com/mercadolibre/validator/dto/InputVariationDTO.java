package com.mercadolibre.validator.dto;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InputVariationDTO {

  public static final String ID = "93955252330";

  public static final String ID_VALUE = "11375715";

  private String id;

  @SerializedName("attribute_combinations")
  private List<InputAttributeDTO> attributeCombinations;

  private List<String> attributes;

  public InputVariationDTO() {
    this.id = ID;
    this.attributeCombinations = new ArrayList<InputAttributeDTO>();
    InputAttributeDTO attribute = new InputAttributeDTO("SIZE", "Talle", ID_VALUE, ID_VALUE);
    this.attributeCombinations.add(attribute);

    this.attributes = new ArrayList<String>();

  }
}
