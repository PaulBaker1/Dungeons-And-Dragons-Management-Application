package com.dmtool.dashboard.dndapi.dto.classDto;

import com.dmtool.dashboard.dndapi.dto.ApiReference;
import com.fasterxml.jackson.annotation.JsonProperty;

public record StartingEquipment(
        EquipmentItem equipment,
        int quantity,
        @JsonProperty("class")
        ApiReference dndClass
) {}
