package com.dmtool.dashboard.campaign.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LevelRange {
    @Schema(description = "Minimum recommended level", example = "1")
    private int minLevel;

    @Schema(description = "Maximum recommended level", example = "5")
    private int maxLevel;
}