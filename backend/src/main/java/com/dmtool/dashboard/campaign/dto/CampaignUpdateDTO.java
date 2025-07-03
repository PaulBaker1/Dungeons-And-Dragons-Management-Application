package com.dmtool.dashboard.campaign.dto;

import com.dmtool.dashboard.campaign.model.CampaignStatus;
import com.dmtool.dashboard.campaign.model.LevelRange;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.util.List;
import java.util.Set;

@Schema(description = "Fields that can be updated in a campaign")
public record CampaignUpdateDTO(
        @Size(min = 3, max = 255)
        @Schema(description = "Updated title", example = "Feywild Reimagined")
        String title,

        @Size(max = 1000)
        @Schema(description = "Updated description", example = "A twisted new path through the Feywild.")
        String description,

        @Schema(description = "Campaign status", example = "ACTIVE")
        CampaignStatus status,

        @Min(1)
        @Max(20)
        @Schema(description = "Minimum level", example = "4")
        Integer minLevel,

        @Min(1)
        @Max(20)
        @Schema(description = "Maximum level", example = "15")
        Integer maxLevel,

        @Schema(description = "Updated tags", example = "[\"dreams\", \"fey\"]")
        Set<@NotBlank @Size(max = 50) String> tags,

        @Schema(description = "Updated factions", example = "[3, 4]")
        Set<@NotNull Long> involvedFactions,

        @Schema(description = "Visibility toggle", example = "true")
        Boolean visibleToPlayers,

        @Schema(description = "Recommended level range text", example = "Levels 4–10")
        LevelRange recommendedLevelRange,

        @Schema(description = "Banner image URL")
        String bannerImageUrl,


        List<@NotBlank String> sections,

        List<@NotBlank String> keyLocations,

        @Schema(description = "Private notes (DM only)", example = "Traps reset every 48 hours")
        String dmNotesPrivate,

        @Schema(description = "Public notes (player-visible)", example = "Discovered ruins in forest")
        String dmNotesPublic,

        @Size(max = 255)
        String adventureCode,

        Boolean pinned
) {
}
