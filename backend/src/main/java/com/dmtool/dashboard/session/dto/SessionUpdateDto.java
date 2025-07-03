package com.dmtool.dashboard.session.dto;

import com.dmtool.dashboard.session.model.Session.SessionStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Payload to update an existing session")
public record SessionUpdateDto(

        @Schema(description = "Title of the session", example = "Into the Crypts")
        String title,

        @Schema(description = "Objective", example = "Explore deeper ruins under the temple")
        String objective,

        @Schema(description = "Session summary", example = "Party encountered a banshee near the sealed vault")
        String summary,

        @Schema(description = "Updated session date", example = "2025-03-25T14:00:00")
        LocalDateTime sessionDate,

        @Schema(description = "Status of the session", example = "COMPLETED")
        SessionStatus status,

        List<AgendaItemDto> agendaItems,
        List<EncounterDto> encounters,
        List<SessionNoteDto> notes,
        List<SessionDecisionDto> decisions,
        List<KeyItemDto> keyItems,

        @Schema(description = "Notes related to upcoming sessions")
                String upcomingNotes,

        @Schema(description = "Notes about past sessions")
        String pastNotes,

        @Schema(description = "Attendance notes or comments")
        String attendanceNotes
) {
}
