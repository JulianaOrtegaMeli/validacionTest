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
public class Variation{
    public String id;
    public List<AttributeCombination> attribute_combinations;
    public List<Object> attributes;
}