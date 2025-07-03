package com.dmtool.dashboard.questtracker.dto;

import com.dmtool.dashboard.questtracker.model.QuestStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateQuestStatusRequest(
        @NotNull QuestStatus status
) {}
