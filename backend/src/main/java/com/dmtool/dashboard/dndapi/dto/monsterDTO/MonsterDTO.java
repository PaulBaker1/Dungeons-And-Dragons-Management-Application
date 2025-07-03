package com.dmtool.dashboard.dndapi.dto.monsterDTO;

import com.dmtool.dashboard.dndapi.dto.deserializer.ArmorClassDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MonsterDTO(
        String index,
        String name,
        String size,
        String type,
        int hit_points,
        List<SrdMonsterDto.ArmorClass> armor_class, // ✅ works with JSON array
        String url
) { }

