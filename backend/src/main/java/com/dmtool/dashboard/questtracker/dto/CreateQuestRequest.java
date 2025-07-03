package com.dmtool.dashboard.questtracker.dto;

import com.dmtool.dashboard.questtracker.model.QuestPriority;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.Instant;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public record CreateQuestRequest(
        String title,
        String description,
        String status,
        String questGiver,       // Added questGiver
        String reward,           // Added reward
        String notes,            // Added notes
        QuestPriority priority,
        boolean visibleToPlayers,
        Set<String> tags,
        Long linkedSessionId,
        Instant completedAt
) {}
