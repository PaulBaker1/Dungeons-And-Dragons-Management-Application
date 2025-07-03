package com.dmtool.dashboard.srd.srdmetadata.dto;

import java.util.List;

public record SrdClassLevelDto(
        String index,
        String url,
        Integer level,
        Integer ability_score_bonuses,
        Integer prof_bonus,
        List<SrdFeatureDto> features,
        SpellcastingDto spellcasting,
        Object class_specific // Adjust with a proper DTO if the structure is known
) {
}
