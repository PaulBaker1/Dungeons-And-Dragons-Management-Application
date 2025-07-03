package com.dmtool.dashboard.campaign.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Lightweight summary of a campaign")
public record CampaignSummaryDTO(
        @Schema(description = "ID of the campaign", example = "42")
        Long id,

        @Schema(description = "Title", example = "Curse of Strahd")
        String title,

        String description,

        @Schema(description = "Objective of the last session", example = "The heroes confront the vampire lord")
        String lastSessionObjective,

        Boolean pinned
) {
}

