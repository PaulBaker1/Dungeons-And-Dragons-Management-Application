package com.dmtool.dashboard.session.dto;

import com.dmtool.dashboard.session.model.Session.SessionStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Full details of a D&D campaign session")
public record SessionDto(

        @Schema(description = "Session ID", example = "1")
        Long id,

        @Schema(description = "Session title", example = "Into the Crypts")
        String title,

        @Schema(description = "Session objective", example = "Investigate undead activity beneath Phlan")
        String objective,

        @Schema(description = "Summary of session events", example = "Party entered the catacombs, faced ghouls and uncovered ancient tombs")
        String summary,

        @Schema(description = "Status of the session", example = "COMPLETED")
        SessionStatus status,

        @Schema(description = "Date and time of the session", example = "2025-03-25T14:00:00")
        LocalDateTime sessionDate,

        @Schema(description = "Agenda items planned for the session")
        List<AgendaItemDto> agendaItems,

        @Schema(description = "Encounters in the session")
        List<EncounterDto> encounters,

        @Schema(description = "Narrative or mechanical notes")
        List<SessionNoteDto> notes,

        @Schema(description = "Key decisions made during the session")
        List<SessionDecisionDto> decisions,

        @Schema(description = "Tracked key items obtained/used during the session")
        List<KeyItemDto> keyItems,

        @Schema(description = "Notes related to upcoming sessions")
                String upcomingNotes,

        @Schema(description = "Notes about past sessions")
        String pastNotes,

        @Schema(description = "Attendance notes or comments")
        String attendanceNotes
) {
}


