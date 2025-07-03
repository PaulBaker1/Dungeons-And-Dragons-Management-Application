package com.dmtool.dashboard.prepitem.dto;

import com.dmtool.dashboard.prepitem.model.PrepItemType;
import com.dmtool.dashboard.prepitem.model.PrepStatus;

import java.time.LocalDateTime;

public record PrepItemDto(
        Long id,
        String title,
        String description,
        PrepItemType type,
        PrepStatus status,
        boolean visibleToPlayers,
        Long sessionId,  // may be null
        Long campaignId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
