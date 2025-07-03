package com.dmtool.dashboard.session.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Special item tracked across session(s)")
public record KeyItemDto(

        @Schema(description = "Item ID")
        Long id,

        @Schema(description = "Name of the item", example = "Bone Charm of Duerra")
        String itemName,

        @Schema(description = "Item description", example = "Allows communication with Duergar spirits")
        String description,

        @Schema(description = "Whether the party acquired this item", example = "true")
        boolean acquired,

        @Schema(description = "Where the item was found", example = "Tomb of the Forgotten King")
        String locationFound,

        @Schema(description = "How the item was used", example = "Used to calm hostile undead")
        String usedIn
) {
}
