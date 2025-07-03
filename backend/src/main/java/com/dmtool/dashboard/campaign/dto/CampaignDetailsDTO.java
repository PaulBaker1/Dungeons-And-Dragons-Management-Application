package com.dmtool.dashboard.campaign.dto;

import com.dmtool.dashboard.campaign.model.CampaignStatus;
import com.dmtool.dashboard.campaign.model.LevelRange;
import com.dmtool.dashboard.character.model.PlayerCharacter;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Schema(description = "Full details of a campaign")
public record CampaignDetailsDTO(
        @Schema(description = "ID of the campaign", example = "101")
        Long id,

        @Schema(description = "Title of the campaign", example = "Rime of the Frostmaiden")
        String title,

        @Schema(description = "Detailed description", example = "Frozen terror grips the land of Icewind Dale.")
        String description,

        @Schema(description = "Whether this campaign is archived", example = "false")
        boolean archived,

        @Schema(description = "Timestamp of last session", example = "2025-03-23T18:00:00")
        LocalDateTime lastSessionDate,

        @Schema(description = "Whether this campaign is pinned", example = "true")
        boolean pinned,

        @Schema(description = "Private notes (DM only)")
        String dmNotesPrivate,

        @Schema(description = "Public notes (player-visible)")
        String dmNotesPublic,
        @Schema(description = "Recommended level range for the campaign", example = "1-5")
        LevelRange recommendedLevelRange,

        @Schema(description = "Key story sections/chapters", example = "[\"Chapter 1: The Frozen Wastes\", \"Chapter 2: Sunblight\"]")
        Set<String> sections,

        @Schema(description = "Important locations in the campaign", example = "[\"Bryn Shander\", \"Ythryn\"]")
        Set<String> keyLocations,

        @Schema(description = "Adventure module code", example = "WDH")
        String adventureCode,

        @Schema(description = "Factions involved in the story", example = "[\"Arcane Brotherhood\", \"Reghed Tribes\"]")
        Set<String> involvedFactions,

        @Schema(description = "Campaign tags/categories", example = "[\"Arctic\", \"Ancient Magic\", \"Feywild\"]")
        Set<String> tags,

        @Schema(description = "Current campaign status",
                example = "ACTIVE",
                allowableValues = {"ACTIVE", "COMPLETED", "ON_HIATUS", "PREPARATION"})
        CampaignStatus status,

        @Schema(description = "Visibility to players", example = "true")
        Boolean visibleToPlayers,

        @Schema(description = "Characters in this campaign")
        List<PlayerCharacter> characters
) {
}
