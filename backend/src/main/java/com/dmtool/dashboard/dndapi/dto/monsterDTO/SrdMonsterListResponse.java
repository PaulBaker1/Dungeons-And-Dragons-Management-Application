package com.dmtool.dashboard.dndapi.dto.monsterDTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SrdMonsterListResponse(
        List<SrdMonsterDto> results
) {
}