package com.dmtool.dashboard.character.dto;

import com.dmtool.dashboard.character.model.Faction;

import java.math.BigDecimal;
import java.util.List;

public record CreateCharacterLogRequest(
        Long sessionId,
        String playerName,
        String characterName,
        String classAndLevel,
        Faction faction, // can be null if not provided
        boolean inspirationUsed,
        int deathSavesFailed,
        List<String> magicItemsFound,
        BigDecimal goldEarned,
        int downtimeGained,
        int renown,
        String customNotes
) {
}
