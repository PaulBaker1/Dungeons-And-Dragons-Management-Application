package com.dmtool.dashboard.dndapi.dto.monsterDTO;


import com.dmtool.dashboard.dndapi.dto.deserializer.ArmorClassDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SrdMonsterDto(
        String index,
        String name,
        String size,
        String type,
        int hit_points,
        @JsonDeserialize(using = ArmorClassDeserializer.class)
        List<ArmorClass> armor_class,
        String url
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static record ArmorClass(
            String type,
            int value
    ) { }
}
