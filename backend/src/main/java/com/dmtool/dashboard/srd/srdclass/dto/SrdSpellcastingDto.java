package com.dmtool.dashboard.srd.srdclass.dto;

import java.util.List;

public record SrdSpellcastingDto(
        String level,
        String spellcasting_ability,
        List<String> cantrips,
        List<String> spells_known
) {}
