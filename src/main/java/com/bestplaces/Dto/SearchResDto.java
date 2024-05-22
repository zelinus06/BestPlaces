package com.bestplaces.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchResDto {
    private String priceRange;
    private String areaRange;
    private String Type;
    private String city;
    private String district;
    private String commune;
}
