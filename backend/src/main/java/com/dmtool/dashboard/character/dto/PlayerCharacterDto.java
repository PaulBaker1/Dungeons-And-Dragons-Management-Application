package com.dmtool.dashboard.character.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record PlayerCharacterDto(
        Long id,
        String playerName,
        String characterName,
        String classAndLevel,
        int experience,
        int level,
        boolean levelOverride,
        int hitPoints,
        int armorClass,
        int spellDC,
        int speed,
        int initiative,
        int strength,
        int dexterity,
        int constitution,
        int intelligence,
        int wisdom,
        int charisma,
        boolean passivePerception,
        boolean passiveInvestigation,
        boolean passiveInsight,
        boolean jackOfAllTrades,
        String controlPlayer,
        List<String> damageResistances,
        List<String> damageVulnerabilities,
        List<String> damageImmunities,
        List<String> conditionImmunities,
        List<String> skillProficiencies,
        String notes,
        String avatar,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime createdAt,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime updatedAt,
        int sessionCount
) {
}