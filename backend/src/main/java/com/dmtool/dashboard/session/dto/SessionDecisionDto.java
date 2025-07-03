package com.dmtool.dashboard.session.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Major player decision and its consequences")
public record SessionDecisionDto(

        @Schema(description = "Decision ID")
        Long id,

        @Schema(description = "Situation that required a choice", example = "Whether to disturb the sealed tomb")
        String decisionPoint,

        @Schema(description = "Player choice made", example = "Opened the tomb")
        String choiceMade,

        @Schema(description = "Outcome from that decision", example = "Unleashed a specter that now stalks the catacombs")
        String consequences
) {
}
