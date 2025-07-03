package com.dmtool.dashboard.prepitem.dto;

import com.dmtool.dashboard.prepitem.model.PrepItemType;

// Fix your CreatePrepItemRequest to have 8 fields:
public record CreatePrepItemRequest(
        String title,
        String description,
        PrepItemType type,
        boolean visibleToPlayers,
        boolean isCritical,
        String fileUrl,    // optional media or asset link
        Long sessionId,    // can be null if unassigned
        Long campaignId
) {}

