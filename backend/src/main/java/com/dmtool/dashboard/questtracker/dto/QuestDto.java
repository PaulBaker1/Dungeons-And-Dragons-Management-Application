package com.dmtool.dashboard.questtracker.dto;

import com.dmtool.dashboard.questtracker.model.QuestPriority;
import com.dmtool.dashboard.questtracker.model.QuestStatus;
import java.time.LocalDateTime;
import java.util.Set;

public record QuestDto(
        Long id,
        String title,
        String description,
        QuestStatus status,
        QuestPriority priority,
        boolean visibleToPlayers,
        Set<String> tags,
        Long linkedSessionId,
        Long campaignId,
        LocalDateTime completedAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String questGiver,      // Added questGiver
        String reward,          // Added reward
        String notes            // Added notes
) {}
