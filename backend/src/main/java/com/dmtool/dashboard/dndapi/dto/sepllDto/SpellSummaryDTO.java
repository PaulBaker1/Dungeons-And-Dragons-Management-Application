package com.dmtool.dashboard.dndapi.dto.sepllDto;

public record SpellSummaryDTO (
        String index,
        String name,
        int level,
        String shortDescription,
        String casting_time,
        String duration
) {
}
