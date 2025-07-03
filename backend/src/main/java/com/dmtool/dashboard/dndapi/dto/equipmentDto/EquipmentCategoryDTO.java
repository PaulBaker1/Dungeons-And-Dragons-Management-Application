package com.dmtool.dashboard.dndapi.dto.equipmentDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EquipmentCategoryDTO(
        String index,
        String name,
        String url
) {}
