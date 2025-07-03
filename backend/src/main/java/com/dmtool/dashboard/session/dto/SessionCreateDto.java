package com.dmtool.dashboard.session.dto;

import com.dmtool.dashboard.session.model.Session.SessionStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Schema(description = "Payload to create a new session")
public record SessionCreateDto(

        @NotBlank
        @Schema(description = "Session title", example = "Into the Crypts")
        String title,

        @NotBlank
        @Schema(description = "Objective of the session", example = "Investigate undead activity beneath Phlan")
        String objective,

        @Size(max = 5000)
        @Schema(description = "Narrative summary", example = "Party entered the catacombs...")
        String summary,

        @NotNull
        @Schema(description = "Session date and time", example = "2025-03-25T14:00:00")
        LocalDateTime sessionDate,

        @NotNull
        @Schema(description = "Status of the session", example = "DRAFT")
        SessionStatus status,

        @Schema(description = "Agenda items for this session")
        List<AgendaItemDto> agendaItems,

        @Schema(description = "Encounters expected or resolved")
        List<EncounterDto> encounters,

        @Schema(description = "Notes for tracking events or DM plans")
        List<SessionNoteDto> notes,

        @Schema(description = "Decisions made by the party")
        List<SessionDecisionDto> decisions,

        @Schema(description = "Items obtained or used")
        List<KeyItemDto> keyItems
) {
        public SessionCreateDto {
        agendaItems = agendaItems != null ? agendaItems : Collections.emptyList();
        encounters = encounters != null ? encounters : Collections.emptyList();
        notes = notes != null ? notes : Collections.emptyList();
        decisions = decisions != null ? decisions : Collections.emptyList();
        keyItems = keyItems != null ? keyItems : Collections.emptyList();
        }
}
