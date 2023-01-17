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
public class RequestColaider {
    public String id;
    public String site_id;
    public String category_id;
    public List<Attribute> attributes;
    public List<Variation> variations;
}
