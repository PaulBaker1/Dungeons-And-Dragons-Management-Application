package com.dmtool.dashboard.dndapi.dto.equipmentDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EquipmentDTO(
        String index,
        String name,
        EquipmentCategoryDTO equipment_category, // ✅ fixed
        String weapon_category,
        String weapon_range,
        String category_range,
        Cost cost,
        Damage damage,
        Range range,
        int weight,
        List<Property> properties,
        String url
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static record Cost(int quantity, String unit) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static record Damage(
            String damage_dice,
            DamageType damage_type
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static record DamageType(
            String index,
            String name,
            String url
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static record Range(
            int normal
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static record Property(
            String index,
            String name,
            String url
    ) {}
}
