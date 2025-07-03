package com.dmtool.dashboard.session.dto;

import com.dmtool.dashboard.session.model.SessionNote.NoteType;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Note attached to the session for narrative or tracking")
public record SessionNoteDto(

        @Schema(description = "Note ID")
        Long id,

        @Schema(description = "Type of note", example = "DM")
        NoteType type,

        @Schema(description = "Note content", example = "Cassyt subtly pushes players to explore deeper")
        String content
) {
}
