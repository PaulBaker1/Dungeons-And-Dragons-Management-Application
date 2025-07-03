package com.dmtool.dashboard.encounterbuilder.dto;

import java.util.List;

public record CreateEncounterRequest(
        String name,
        String notes,
        Long sessionId,
        List<CombatantDto> combatants
) {}
