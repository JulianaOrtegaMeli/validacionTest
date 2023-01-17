package com.mercadolibre.validator.dto;

import com.google.gson.annotations.*;
import lombok.*;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CauseDTO {

  @Expose
  @SerializedName("custom_data")
  public String customData;

  public String code;

  public String message;

  public String type;

  @Expose
  @SerializedName("cause_id")
  public float causeId;

  public List<String> references = new ArrayList<String>();

  private String department;

  private String validation;
}
