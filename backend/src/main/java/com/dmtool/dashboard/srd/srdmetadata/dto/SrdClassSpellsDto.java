package com.dmtool.dashboard.srd.srdmetadata.dto;

import java.util.List;

public record SrdClassSpellsDto(
        Integer count,
        List<SrdSpellDto> results
) {
}
