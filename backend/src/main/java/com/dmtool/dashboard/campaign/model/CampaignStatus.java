package com.dmtool.dashboard.campaign.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Status of a campaign")
public enum CampaignStatus {
    @Schema(
            description = "Campaign is in preparation phase, not yet visible to players",
            example = "DRAFT"
    )
    DRAFT,

    @Schema(
            description = "Campaign is currently running and active",
            example = "ACTIVE"
    )
    ACTIVE,

    @Schema(
            description = "Campaign has reached its conclusion",
            example = "COMPLETED"
    )
    COMPLETED,

    @Schema(
            description = "Campaign is archived and hidden from default views",
            example = "ARCHIVED"
    )
    ARCHIVED
}