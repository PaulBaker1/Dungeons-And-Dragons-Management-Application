package com.dmtool.dashboard.dndapi.dto.classDto;

import com.dmtool.dashboard.dndapi.dto.ApiReference;

public record EquipmentItem(
        ApiReference equipment,
        int quantity
) {}
