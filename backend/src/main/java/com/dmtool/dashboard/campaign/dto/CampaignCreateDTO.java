package com.dmtool.dashboard.campaign.dto;

import com.dmtool.dashboard.campaign.model.CampaignStatus;
import com.dmtool.dashboard.campaign.model.LevelRange;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.util.List;
import java.util.Set;

@Schema(description = "Payload to create a new campaign")
public record CampaignCreateDTO(
        @NotBlank
        @Size(min = 3, max = 255)
        @Schema(description = "Title of the campaign", example = "Rise of the Necromancer")
        String title,

        @NotBlank
        @Size(max = 1000)
        @Schema(description = "Campaign description", example = "Necromancers rise from the ruins of Ashvalen.")
        String description,

        @NotNull
        @Schema(description = "Initial status of campaign", example = "DRAFT")
        CampaignStatus status,

        @Min(1)
        @Max(20)
        @Schema(description = "Minimum player level", example = "3")
        int minLevel,

        @Min(1)
        @Max(20)
        @Schema(description = "Maximum player level", example = "10")
        int maxLevel,

        @Size(max = 255)
        @Schema(description = "Adventure code for player access", example = "XYZ-123-AB")
        String adventureCode,

        @NotNull
        @Schema(description = "Tags (unique, lowercase)", example = "[\"epic\", \"low-magic\"]")
        Set<@NotBlank @Size(max = 50) String> tags,

        @NotNull
        @Schema(description = "Involved faction IDs", example = "[1, 2]")
        Set<@NotNull @Min(1) Long> involvedFactions,

        @Schema(description = "Can players view this campaign?", example = "true")
        boolean visibleToPlayers,

        @Schema(description = "Major sections of the campaign", example = "[\"Act I: The Fall\", \"Act II: The Rise\"]")
        List<@NotBlank String> sections,

        @Schema(description = "Key locations within the campaign", example = "[\"Shadowfell Keep\", \"Moonbridge\"]")
        List<@NotBlank String> keyLocations,

        @Schema(description = "Is the campaign archived?", example = "false")
        boolean archived,

        @Schema(description = "Commanded level range for the Campaig")
        LevelRange levelRange
) {
}
