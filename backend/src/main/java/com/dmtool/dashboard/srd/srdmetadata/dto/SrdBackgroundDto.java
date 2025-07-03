package com.dmtool.dashboard.srd.srdmetadata.dto;

import java.util.List;

public record SrdBackgroundDto(
        String index,
        String name,
        List<String> desc,
        List<ApiReference> proficiencies,
        List<String> equipment
) {
}
