package com.dmtool.dashboard.session.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Item on the session's agenda")
public record AgendaItemDto(

        @Schema(description = "Agenda item ID")
        Long id,

        @Schema(description = "Content of the agenda item", example = "Explore the catacombs beneath Valhingen Graveyard")
        String content,

        @Schema(description = "Is this item visible only to the DM?", example = "true")
        boolean dmOnly,

        @Schema(description = "Order of the agenda item", example = "1")
        int order
) {
}
