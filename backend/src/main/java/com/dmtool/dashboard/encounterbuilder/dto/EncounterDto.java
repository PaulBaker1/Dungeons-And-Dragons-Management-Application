package com.dmtool.dashboard.encounterbuilder.dto;

import java.time.LocalDateTime;
import java.util.List;

public record EncounterDto(
        Long id,
        String name,
        String notes,
        Long sessionId,
        List<CombatantDto> combatants,
        int totalXP,
        double challengeRating,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
