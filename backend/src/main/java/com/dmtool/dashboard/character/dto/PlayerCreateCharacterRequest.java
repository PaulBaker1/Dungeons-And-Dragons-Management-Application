package com.dmtool.dashboard.character.dto;

import com.dmtool.dashboard.campaign.model.Campaign;
import lombok.Builder;
import org.hibernate.Cache;

import java.util.List;

@Builder
public record PlayerCreateCharacterRequest(
        String playerName,
        String characterName,
        String classAndLevel,
        String controlPlayer,
        String avatar,
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
        boolean playerControlled,
        List<String> damageResistances,
        List<String> damageVulnerabilities,
        List<String> damageImmunities,
        List<String> conditionImmunities,
        List<String> skillProficiencies,
        Long campaignId,
        String notes
) {
    public boolean getCampaignId() {
        return campaignId != null;
    }
}
