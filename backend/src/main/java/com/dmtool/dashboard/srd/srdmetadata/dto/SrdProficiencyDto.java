package com.dmtool.dashboard.srd.srdmetadata.dto;

import java.util.List;

public record SrdProficiencyDto(
        String index,
        String name,
        List<String> desc
) {
}
