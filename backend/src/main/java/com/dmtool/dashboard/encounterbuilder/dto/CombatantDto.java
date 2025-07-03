package com.dmtool.dashboard.encounterbuilder.dto;

import java.util.List;

public record CombatantDto(
        Long id,  // may be null for new combatants
        String name,
        int hp,
        int xp,
        double cr,
        int initiative,
        boolean isTemplate,
        int ac,
        int quantity,
        boolean legendary,
        List<String> actions,
        List<String> specialAbilities
) {}
