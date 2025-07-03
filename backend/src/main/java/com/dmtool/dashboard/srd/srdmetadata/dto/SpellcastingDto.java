package com.dmtool.dashboard.srd.srdmetadata.dto;

public record SpellcastingDto(
        Integer cantrips_known,
        Integer spells_known,
        Integer spell_slots_level_1,
        Integer spell_slots_level_2,
        Integer spell_slots_level_3,
        Integer spell_slots_level_4,
        Integer spell_slots_level_5,
        Integer spell_slots_level_6,
        Integer spell_slots_level_7,
        Integer spell_slots_level_8,
        Integer spell_slots_level_9,
        Object class_specific // Adjust as needed
) {
}
