package com.dmtool.dashboard.session.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Combat or narrative encounter from the session")
public record EncounterDto(

        @Schema(description = "Encounter ID")
        Long id,

        @Schema(description = "Name of the encounter", example = "Ghoul Ambush")
        String name,

        @Schema(description = "Location of the encounter", example = "Hall of Bones")
        String location,

        @Schema(description = "Estimated difficulty", example = "Medium")
        String estimatedDifficulty,

        @Schema(description = "Outcome of the encounter", example = "Ghouls defeated; party unharmed")
        String outcome
) {
}
