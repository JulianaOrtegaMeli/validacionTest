package com.mercadolibre.fashion.validator.plugin.dto;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InputJsonDTO {

  private static final String ID = "MLA933769272";

  private String id;

  @SerializedName("site_id")
  private String siteId;

  @SerializedName("category_id")
  private String categoryId;

  private List<InputAttributeDTO> attributes;

  private List<InputVariationDTO> variations;

  /**
   * Método constructor.
   * @param siteId Es el id del site
   * @param categoryId Es el id de la category
   */
  public InputJsonDTO(String siteId, String categoryId) {
    this.id = ID;
    this.siteId = siteId;
    this.categoryId = categoryId;

    this.attributes = new ArrayList<InputAttributeDTO>();
    InputAttributeDTO ageGroup = new InputAttributeDTO("AGE_GROUP", "Edad");
    InputAttributeDTO gender = new InputAttributeDTO("GENDER", "Género");
    this.attributes.add(ageGroup);
    this.attributes.add(gender);

    this.variations = new ArrayList<InputVariationDTO>();
    InputVariationDTO variation = new InputVariationDTO();
    this.variations.add(variation);
  }

}
