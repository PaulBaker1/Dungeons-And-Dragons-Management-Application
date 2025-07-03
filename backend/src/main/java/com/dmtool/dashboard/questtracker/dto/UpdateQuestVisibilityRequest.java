package com.dmtool.dashboard.questtracker.dto;

import jakarta.validation.constraints.NotNull;

public record UpdateQuestVisibilityRequest(
        @NotNull Boolean visibleToPlayers
) {}
