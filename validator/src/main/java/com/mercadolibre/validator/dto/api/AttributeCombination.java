package com.mercadolibre.validator.dto.api;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AttributeCombination {
    public String id;
    public String name;
    public String value_id;
    public String value_name;
    public List<Value> values;
}